package com.example.restdemo.repository;

import com.example.restdemo.dto.SearchTaskDto;
import com.example.restdemo.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;


public class TaskRepositoryImpl implements CustomTask {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<Task> getFilteredTasks(SearchTaskDto searchTaskDto, Pageable pageable) {

        Query query = new Query();
        addPagerToQuery(query, pageable);

        Criteria criteria = makeCriteria(searchTaskDto);
        query.addCriteria(criteria);
        Query countQuery = new Query(criteria);

        List<Task> tasks = mongoTemplate.find(query, Task.class);
        long total = mongoTemplate.count(countQuery, Task.class);
        PageImpl<Task> page = new PageImpl<>(tasks, pageable, total);

        return page;
    }

    @Override
    public List<Task> getFilteredTasks(SearchTaskDto searchTaskDto, Sort sort) {

        Query query = new Query();
        addSortToQuery(query, sort);

        Criteria criteria = makeCriteria(searchTaskDto);
        query.addCriteria(criteria);
        List<Task> tasks = mongoTemplate.find(query, Task.class);

        return tasks;
    }


    @Override
    public List<Task> getFilteredTasks(SearchTaskDto searchTaskDto) {

        return getFilteredTasks(searchTaskDto, Sort.unsorted());
    }

    private Criteria makeCriteria(SearchTaskDto searchTaskDto) {

        ArrayList<Criteria> criteriaList = new ArrayList<>();

        if(searchTaskDto.getLikeName() != null)
            criteriaList.add(Criteria.where("name").regex(searchTaskDto.getLikeName()));

        if(searchTaskDto.getFromStartDate() != null)
            criteriaList.add(Criteria.where("startDate").gt(searchTaskDto.getFromStartDate()));

        if(searchTaskDto.getToStartDate() != null)
            criteriaList.add(Criteria.where("startDate").lt(searchTaskDto.getToStartDate()));

        if(searchTaskDto.getFromFinishDate() != null)
            criteriaList.add(Criteria.where("finishDate").gt(searchTaskDto.getFromFinishDate()));

        if(searchTaskDto.getToFinishDate() != null)
            criteriaList.add(Criteria.where("finishDate").lt(searchTaskDto.getToFinishDate()));

        if (criteriaList.isEmpty())
            return new Criteria();

        return new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
    }

    private Query addPagerToQuery(Query query, Pageable pageable){

        query.skip(pageable.getOffset());
        query.limit(pageable.getPageSize());
        query.with(pageable.getSort());

        return query;
    }

    private Query addSortToQuery(Query query, Sort sort){

        query.with(sort);

        return query;
    }
}
