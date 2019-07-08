package com.common.irendercore.controller.impl;

import com.common.irendercore.controller.BaseApi;
import com.common.irendercore.dto.MyList;
import com.common.irendercore.service.BaseService;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public abstract class BaseApiImpl<D> implements BaseApi<D> {

  protected abstract BaseService<D> getBaseService();

  @Override
  @PostMapping
  public ResponseEntity<D> create(@Valid @RequestBody D objectDTO) {
    objectDTO = getBaseService().create(objectDTO);
    return ResponseEntity.ok(objectDTO);
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") String id) {
    D object = getBaseService().getById(id);
    if (object == null) {
      return ResponseEntity.notFound().build();
    }
    getBaseService().delete(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<D> getById(
          @ApiParam(value = "A unique identifier", required = true) @PathVariable("id") String id) {
    D object = getBaseService().getById(id);
    return object == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(object);
  }

  @Override
  @GetMapping
  public ResponseEntity<List<D>> getByIds(
          @ApiParam(value = "List of id.") @RequestParam(value = "ids", required = false) List<String> ids) {
    List<D> objects = ids == null ? getBaseService().getAll() : getBaseService().getByIds(ids);
    return ResponseEntity.ok(objects);
  }

  @Override
  @GetMapping("/search")
  public ResponseEntity<MyList<D>> search(
          @ApiParam(value = "search filter", required = true) @RequestParam("filter") String filter,
          @ApiParam(value = "search sort", required = false) @RequestParam("sort") String sort,
          @ApiParam(value = "Start from 0") @Valid @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
          @ApiParam(value = "") @Valid @RequestParam(value = "pageSize", required = false, defaultValue = "12") Integer pageSize
  ) {
    MyList<D> myList = getBaseService().search(filter, sort, pageIndex, pageSize);
    return ResponseEntity.ok(myList);
  }

  @Override
  @PutMapping("/{id}")
  public ResponseEntity<Void> update(
          @ApiParam(required = true) @Valid @RequestBody D objectDTO,
          @ApiParam(value = "A unique identifier", required = true) @PathVariable("id") String id) {
    D object = getBaseService().getById(id);
    if (object == null) {
      return ResponseEntity.notFound().build();
    }
    getBaseService().update(id, objectDTO);
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }
}
