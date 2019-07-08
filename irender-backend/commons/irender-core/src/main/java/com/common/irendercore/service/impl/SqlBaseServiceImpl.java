package vn.edu.topica.topkid.core.service.impl;

import com.common.irendercore.dto.MyList;
import com.common.irendercore.dto.MyPage;
import com.common.irendercore.service.BaseService;
import com.common.irendersql.irender.sql.mapper.BaseMapper;
import com.common.irendersql.sql.query.CustomRsqlVisitor;
import com.common.irendersql.sql.repository.SqlBaseRepository;
import com.common.irendersql.sql.repository.entity.MyEntity;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class SqlBaseServiceImpl<D, E extends MyEntity> implements BaseService<D> {
    protected abstract SqlBaseRepository<E> getBaseRepository();

    protected abstract BaseMapper<D, E> getBaseMapper();

    @Override
    public D create(D dto) {
        E entity = getBaseMapper().dtoToEntity(dto);
        entity = getBaseRepository().save(entity);
        return getBaseMapper().entityToDto(entity);
    }

    @Override
    public void update(String id, D d) {
        E entity = getBaseMapper().dtoToEntity(d);
        entity.setId(id);

        getBaseRepository().save(entity);
    }

    @Override
    public void delete(String id) {
        getBaseRepository().deleteById(id);
    }

    @Override
    public D getById(String id) {
        return getBaseRepository().findById(id).flatMap(e ->
            Optional.of(getBaseMapper().entityToDto(e))
        ).orElse(null);
    }

    @Override
    public List<D> getByIds(List<String> ids) {
        List<D> ds = new ArrayList<>();
        Iterable<E> es = getBaseRepository().findAllById(ids);
        es.forEach(e -> ds.add(getBaseMapper().entityToDto(e)));
        return ds;
    }

    @Override
    public List<D> getAll() {
        List<D> ds = new ArrayList<>();
        Iterable<E> es = getBaseRepository().findAll();
        es.forEach(e -> ds.add(getBaseMapper().entityToDto(e)));
        return ds;
    }

    @Override
    public MyList<D> search(String filter, String sort, Integer pageIndex, Integer pageSize) {
        Node rootNode = new RSQLParser().parse(filter);
        Specification<E> spec = rootNode.accept(new CustomRsqlVisitor<>());

        Sort orders = new Sort(Sort.Direction.DESC, "id");

        if (StringUtils.isNotEmpty(sort)) {
            String[] sortList = sort.split(";");
            String firstSort = sortList[0];
            orders = new Sort(firstSort.startsWith("-") ? Sort.Direction.DESC : Sort.Direction.ASC, firstSort.replaceAll("^-", ""));

            for (int i = 1; i < sortList.length; i++) {
                String sortElement = sortList[i];
                orders = orders.and(new Sort(sortElement.startsWith("-") ? Sort.Direction.DESC : Sort.Direction.ASC, sortElement.replaceAll("^-", "")));
            }
        }

        Pageable pageable = PageRequest.of(pageIndex, pageSize, orders);

        Page<E> page = getBaseRepository().findAll(spec, pageable);

        MyList<D> myList = new MyList<>();
        List<E> contents = page.getContent();

        List<D> results = new ArrayList<>();
        contents.forEach(content -> results.add(getBaseMapper().entityToDto(content)));
        myList.setData(results);
        myList.setMyPage(MyPage.of(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages()));

        return myList;
    }
}
