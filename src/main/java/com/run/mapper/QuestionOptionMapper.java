package com.run.mapper;

import com.run.pojo.Question;
import com.run.pojo.QuestionOption;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigInteger;

/**
 * @author: edxuanlen
 * @date: 2020-04-07 17:09
 * @version: 1.0
 **/
@Mapper
public interface QuestionOptionMapper {

    /**
     * 插入问题对应的选项
     * @param questionOption 问题对象
     * @return 插入是否成功
     */
    int insertQuestionOption(QuestionOption questionOption);

    /**
     * 通过option id更新问题描述
     * @param opId option id
     * @param opDescribe option describe
     * @return update status
     * @throws Exception update error
     */
    boolean updateOfQuestionOptionById (BigInteger opId, String opDescribe) throws Exception;


    /**
     * 通过option id 增加选择人数
     * @param opId option id
     * @return update status
     * @throws Exception update error
     */
    boolean updateQuestionOptionSelectNumber (BigInteger opId) throws Exception;

}
