package com.dictionaryai.utils;

import com.dictionaryai.model.Language;

public class Constants {

    public static final String MODEL_3_5_TURBO = "gpt-3.5-turbo";
    public static final String MODEL_4_O_MINI = "gpt-4o-mini";

    public static class Prompts {

        private static final String TRANSLATOR = """
                You are a translator from %1$s to %2$s.\
                Provide translation for a given word or sentence.\
                Response will be in JSON format with array field called translations ordered by most frequently used.\
                If a given word does not exists in %1$s return empty array.""";


        public static String translator(Language from, Language to) {
            return String.format(TRANSLATOR, from, to);
        }

    }

}
