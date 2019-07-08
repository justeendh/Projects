package com.common.irendercore.controller;

import com.common.irendercore.dto.MyList;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public interface BaseApi<D> {

  @ApiOperation(value = "Create", authorizations = {
      @Authorization(value = "JwtToken")
  }, tags = {})
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "Successful response.")})
  @PostMapping(consumes = {"application/json"})
  ResponseEntity<D> create(@Valid @RequestBody D objectDTO);


  @ApiOperation(value = "Delete", authorizations = {
      @Authorization(value = "JwtToken")
  }, tags = {})
  @ApiResponses(value = {
      @ApiResponse(code = 204, message = "Successful response.")})
  @DeleteMapping(value = "/{id}")
  ResponseEntity<Void> delete(@PathVariable("id") String id);


  @ApiOperation(value = "Get", authorizations = {
      @Authorization(value = "JwtToken")
  }, tags = {})
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successful response")})
  @GetMapping(value = "/{id}",
      produces = {"application/json"})
  ResponseEntity<D> getById(
          @ApiParam(value = "A unique identifier", required = true) @PathVariable("id") String id);


  @ApiOperation(value = "Get by ids", responseContainer = "List", authorizations = {
      @Authorization(value = "JwtToken")
  }, tags = {})
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successful response.")})
  @GetMapping(value = "/ids",
      produces = {"application/json"})
  ResponseEntity<List<D>> getByIds(
          @ApiParam(value = "List of id.", required = true) @RequestParam("ids") List<String> ids);


  @ApiOperation(value = "Search", responseContainer = "List", authorizations = {
      @Authorization(value = "JwtToken")
  }, tags = {})
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successful response", responseContainer = "List")})
  @GetMapping(value = "/search",produces = {"application/json"})
  ResponseEntity<MyList<D>> search(
          @ApiParam(value = "search filter", required = true) @RequestParam("filter") String filter,
          @ApiParam(value = "search sort", required = false) @RequestParam("sort") String sort,
          @ApiParam(value = "Start from 0") @Valid @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Integer pageIndex,
          @ApiParam(value = "") @Valid @RequestParam(value = "pageSize", required = false, defaultValue = "12") Integer pageSize
  );


  @ApiOperation(value = "Update", authorizations = {
      @Authorization(value = "JwtToken")
  }, tags = {})
  @ApiResponses(value = {
      @ApiResponse(code = 202, message = "Successful response.")})
  @PutMapping(value = "/{id}",
      consumes = {"application/json"})
  ResponseEntity<Void> update(
          @ApiParam(required = true) @Valid @RequestBody D objectDTO,
          @ApiParam(value = "A unique identifier", required = true) @PathVariable("id") String id);
}
