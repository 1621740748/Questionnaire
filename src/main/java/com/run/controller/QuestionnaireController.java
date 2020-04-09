package com.run.controller;

import com.run.mapper.QuestionMapper;
import com.run.mapper.QuestionOptionMapper;
import com.run.mapper.QuestionnaireMapper;
import com.run.mapper.SubjectiveQuestionAnswerMapper;
import com.run.pojo.Question;
import com.run.pojo.QuestionOption;
import com.run.pojo.Questionnaire;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


@Controller
@RequestMapping("/questionnaire")
@EnableSwagger2 // 启动swagger注解
// api-value：定义名称，如果没有定义，则默认显示类名
@Api(value = "Questionnaire Controller", tags = "Questionnaire Control Tag")
public class QuestionnaireController {

    @Autowired
    private QuestionnaireMapper questionnaireMapper;

    @Autowired
    private QuestionOptionMapper questionOptionMapper;

    @Autowired
    private SubjectiveQuestionAnswerMapper subjectiveQuestionAnswerMapper;

    @Autowired
    private QuestionMapper questionMapper;

    static final String QUESTION = "question";
    static final String OPTION = "option";
    static final String ROOT = "root";


    @RequestMapping(value = {"/update"}, method = RequestMethod.POST)
//    @ResponseBody
    public void updateForQuestionnaire(Model model,
                                         HttpServletRequest request,
                                         HttpServletResponse response) throws Exception{


        Map<String, String[]> m = request.getParameterMap();
        String type = String.valueOf(m.get("type")[0]);
        BigInteger questionnaireId = new BigInteger(m.get("id")[0]);
        System.out.println("questionnaire id: " + questionnaireId);

        if (type != null){
            System.out.println("questionnaire type:" + type);
            if (type.equals("view")){
                for (Map.Entry<String, String[]> entry : m.entrySet()){
                    String key = entry.getKey();
                    System.out.println("key : " + key);
                    if(Pattern.matches("text.*", key)){
                        String text = entry.getValue()[0];
                        BigInteger qId = new BigInteger(key.split("_")[1]);
                        subjectiveQuestionAnswerMapper.insertNewSubjectiveQuestionAnswer(qId, text);
//                        System.out.println("text: " + text + "id: " + qId);
                    }
                    else if (!key.equals("type") && !key.equals("id")){
                        String[] values = entry.getValue();
                        for (String value : values){
                            BigInteger opId = new BigInteger(value);
                            questionOptionMapper.updateQuestionOptionSelectNumber(opId);
//                            System.out.println(value);
                        }
                    }
                }

            } else {
                for (Map.Entry<String, String[]> entry: m.entrySet()) {
                    String key = entry.getKey();

                    if ("type".equals(key) || "id".equals(key)){
                        continue;
                    }
                    String[] postNameSplit = key.split("_");

                    if (postNameSplit[0].equals(QUESTION)) {
                        BigInteger questionId =
                                new BigInteger(postNameSplit[1]);
                        String questionDescribe = entry.getValue()[0].split(". ")[1];
                        System.out.println("question" + questionId + ": " + questionDescribe);

                        questionMapper.updateQuestionDescribe(questionId,
                                questionDescribe.trim());
                    } else {
                        BigInteger optionId = new BigInteger(postNameSplit[1]);
                        String optionDescribe = entry.getValue()[0];
                        System.out.println("option" + optionId + ": " + optionDescribe);
                        questionOptionMapper.updateOfQuestionOptionById(optionId, optionDescribe.trim());
                    }
                }
            }
        } else{
            System.out.println("No type");
        }
        response.sendRedirect("../admin");
    }

    @RequestMapping(value = {"/{id}/{type}"})
    public String getAllQuestions(@PathVariable("id") BigInteger id, @PathVariable("type") String type, Model model, HttpServletRequest request) throws Exception {
        //获取所有问题信息
        List<Question> list = questionnaireMapper.getAllQuestions(id);
        //获取问卷名
        Questionnaire questionnaire = questionnaireMapper.getQuestionnaireById(id);


        String username = null;
        try{
            username = request.getUserPrincipal().getName();
        } catch (Exception e){
            System.out.println(e);
        }

        //把问题信息放入Attribute
        model.addAttribute("questionnaire_id", id);
        model.addAttribute("questionnaire_name", questionnaire.getName());
        model.addAttribute("allquestions", list);

        if ( username != null && username.equals("root") && type.equals("view")){
            username = "viewer";
            System.out.println(username);
        }

        model.addAttribute("username", username);
        String compareType = "change";
        type = type.toString();

        if (type.equals(compareType)) {
            if ( username != null && username.equals(username)){
                model.addAttribute("username", username);
            } else {
                return "/error/404";
            }
        }

        //循环每一个问题获取每一个问题的选项
        int total = list.size();
        BigInteger QId;
        String s = "options";
        List<QuestionOption> option_list;
        HashMap<BigInteger, List<QuestionOption>> M = new HashMap<BigInteger, List<QuestionOption>>();
        for(int i = 0; i < total; i ++) {
            QId = list.get(i).getQId();
            option_list = questionnaireMapper.getQuestionOptionById(QId);
            M.put(QId, option_list);
        }
        model.addAttribute(s , M);
        return "questionnaire";
    }


}
