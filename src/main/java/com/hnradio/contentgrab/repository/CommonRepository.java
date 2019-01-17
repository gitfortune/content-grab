package com.hnradio.contentgrab.repository;

import com.hnradio.contentgrab.entity.CommonDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CommonRepository extends JpaRepository<CommonDO,Integer> {

   CommonDO findById(int l);

   @Transactional
   @Modifying
   @Query(nativeQuery = true,value = "UPDATE common SET used_id = :id WHERE id=1 ")
   int updateUsedId(long id);
}
