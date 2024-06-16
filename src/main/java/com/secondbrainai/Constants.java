package com.secondbrainai;

public class Constants {

    public static final String MODEL_3_5_TURBO = "gpt-3.5-turbo";

    public static class Prompts {

        public static final String TRANSLATOR = """
                You are a translator from English to Czech.
                Provide translation for a given word or sentence.
                Response will be in JSON format with array field called translations.""";


    }

}
