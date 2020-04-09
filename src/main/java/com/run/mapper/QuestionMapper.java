package com.run.mapper;

import com.run.pojo.Question;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigInteger;

/**
 * @author: edxuanlen
 * @date: 2020-04-07 15:37
 * @version 1.0
 **/
@Mapper
public interface QuestionMapper {

    /**
     * 插入一个问题
     * @param question 问题
     * @throws Exception 查询异常
     */
    void insertQuestion(Question question) throws Exception;


    /**
     * 更新问题描述
     * @param qId question id
     * @param qDescribe question describe
     * @return update status
     * @throws Exception update error
     */
    boolean updateQuestionDescribe(BigInteger qId, String qDescribe) throws Exception;

}
