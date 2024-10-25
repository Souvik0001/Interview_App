package com.AzureOpenAI.Hackathon.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

//@Data
//@NoArgsConstructor
//public class ResponseQuestionDto {
//    String interview_question;
//}

public record ResponseQuestionDto (
    String interview_question
){}
