package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class BatchService {
    @PersistenceContext
    private EntityManager entityManager;

    //配置文件中每次批量提交的数量
//    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private long batchSize=10000;

    public <T> void batchInsert(List<T> list) {
        if (!ObjectUtils.isEmpty(list)){
            for (int i = 0; i < list.size(); i++) {
                entityManager.persist(list.get(i));
                if (i % batchSize == 0) {
                    entityManager.flush();
                    entityManager.clear();
                }
            }
            entityManager.flush();
            entityManager.clear();
        }
    }

    public <T> void batchUpdate(List<T> list) {
        if (!ObjectUtils.isEmpty(list)){
            for (int i = 0; i < list.size(); i++) {
                entityManager.merge(list.get(i));
                if (i % batchSize == 0) {
                    entityManager.flush();
                    entityManager.clear();
                }
            }
            entityManager.flush();
            entityManager.clear();
        }
    }

}
