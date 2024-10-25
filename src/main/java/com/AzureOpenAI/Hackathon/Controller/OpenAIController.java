package com.AzureOpenAI.Hackathon.Controller;

import com.AzureOpenAI.Hackathon.dtos.ResponseQuestionDto;
import com.AzureOpenAI.Hackathon.dtos.ResponseReviewDto;
import com.azure.json.implementation.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class OpenAIController {

    private ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(OpenAIController.class);

    @Autowired
    private AzureOpenAiChatModel azureOpenAiChatModel;

//    ResponseQuestionDto responseQuestionDto;

    @PostMapping("/question")
    public ResponseEntity<?> getQuestions(@RequestParam String category,@RequestParam String parameter) {
        try {
            // Define the template for the question
            String template = """
                Provide a JSON with 1 {category} interview question of {parameter} difficulty.
                Only include: question.
                {format}
            """;

            // Create an output parser for ResponseQuestionDto
            var outputParser = new BeanOutputParser<>(ResponseQuestionDto.class);
            String format = outputParser.getFormat();

            // Create a prompt template with the category and format
            PromptTemplate promptTemplate = new PromptTemplate(template, Map.of("category", category,"parameter",parameter ,"format", format));
            Prompt prompt = promptTemplate.create();

            // Log the prompt for debugging
            logger.info("Prompt: " + prompt);

            // Call the AI model to get the result
            Generation generation = azureOpenAiChatModel.call(prompt).getResult();

            // Log the response for debugging
            logger.info("Received response: " + generation);

            // Parse the response to ResponseQuestionDto
            ResponseQuestionDto questionDto = outputParser.parse(generation.getOutput().getContent());

            // Return the parsed DTO as a JSON response
            return ResponseEntity.ok(questionDto);


        } catch (Exception e) {
            // Handle any other generic exceptions
            logger.error("An unexpected error occurred: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }


    @PostMapping("/review")
    public ResponseEntity<?> getReview(@RequestParam String question, @RequestParam String responseQ) {
        try {
            // Define the template for the review
            String template = """
                Provide a JSON review for the response: {response} to the question: {question}.
                The JSON must include:
                - "review" (a summary of the answer),
                - "goodnessQuotient" (either "good" or "bad"),
                - "nextQuestionDifficulty" (set to "hard" if goodnessQuotient is "good", otherwise "easy").
                Exclude the question and response from the review.
                {format}
            """;

            // Create an output parser for ResponseReviewDto
            var outputParser = new BeanOutputParser<>(ResponseReviewDto.class);
            String format = outputParser.getFormat();

            // Create a prompt template with the provided question, response, and format
            PromptTemplate promptTemplate = new PromptTemplate(template, Map.of("response", responseQ, "question", question, "format", format));
            Prompt prompt = promptTemplate.create();

            // Log the prompt for debugging
            logger.info("Prompt: " + prompt);

            // Call the AI model to get the result
            Generation generation = azureOpenAiChatModel.call(prompt).getResult();

            // Log the response for debugging
            logger.info("Received response: " + generation);

            // Parse the AI response to ResponseReviewDto
            ResponseReviewDto reviewDto = outputParser.parse(generation.getOutput().getContent());

            // Return the parsed DTO as a JSON response
            return ResponseEntity.ok(reviewDto);

        }  catch (Exception e) {
            // Handle any other generic exceptions
            logger.error("An unexpected error occurred: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }



}
