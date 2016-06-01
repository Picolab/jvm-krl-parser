    import java.util.*;
    import org.antlr.runtime.*;
    import java.io.*;
    import org.json.*;

    class Antlr_ {
        public Antlr_() {
        }


        /**
          * Given the input string with escaped unicode characters convert them
          * to their native unicode characters and return the result. This is quite
          * similar to the functionality found in property file handling. White space
          * escapes are not processed (as they are consumed by the template library).
          * Any bogus escape codes will remain in place.
          * <p>
          * When files are provided in another encoding, they can be converted to ascii using
          * the native2ascii tool (a java sdk binary). This tool will escape all the
          * non Latin1 ASCII characters and convert the file into Latin1 with unicode escapes.
          *
          * @param source
          *      string with unicode escapes
          * @return
          *      string with all unicode characters, all unicode escapes expanded.
          *
          * @author Caleb Lyness
          */
        private static String unescapeUnicode(String source) {
            /* could use regular expression, but not this time... */
            final int srcLen = source.length();
            char c;

            StringBuffer buffer = new StringBuffer(srcLen);

            // Must have format \\uXXXX where XXXX is a hexadecimal number
            int i=0;
            while (i <srcLen-5) {

                    c = source.charAt(i++);

                    if (c=='\\') {
                        char nc = source.charAt(i);
                        if (nc == 'u') {

                            // Now we found the u we need to find another 4 hex digits
                            // Note: shifting left by 4 is the same as multiplying by 16
                            int v = 0; // Accumulator
                            for (int j=1; j < 5; j++) {
                                nc = source.charAt(i+j);
                                switch(nc)
                                {
                                    case 48: // '0'
                                    case 49: // '1'
                                    case 50: // '2'
                                    case 51: // '3'
                                    case 52: // '4'
                                    case 53: // '5'
                                    case 54: // '6'
                                    case 55: // '7'
                                    case 56: // '8'
                                    case 57: // '9'
                                        v = ((v << 4) + nc) - 48;
                                        break;

                                    case 97: // 'a'
                                    case 98: // 'b'
                                    case 99: // 'c'
                                    case 100: // 'd'
                                    case 101: // 'e'
                                    case 102: // 'f'
                                        v = ((v << 4)+10+nc)-97;
                                        break;

                                    case 65: // 'A'
                                    case 66: // 'B'
                                    case 67: // 'C'
                                    case 68: // 'D'
                                    case 69: // 'E'
                                    case 70: // 'F'
                                        v = ((v << 4)+10+nc)-65;
                                        break;
                                    default:
                                        // almost but no go
                                        j = 6;  // terminate the loop
                                        v = 0;  // clear the accumulator
                                        break;
                                }
                            } // for each of the 4 digits

                            if (v > 0) {      // We got a full conversion
                                c = (char)v;  // Use the converted char
                                i += 5;      // skip the numeric values
                            }
                        }
                    }
                    buffer.append(c);
                }

            // Fill in the remaining characters from the buffer
            while (i <srcLen) {
                buffer.append(source.charAt(i++));
            }
            return buffer.toString();
        }


        public String ruleset(String krl) throws org.antlr.runtime.RecognitionException {
            try {
                org.antlr.runtime.ANTLRStringStream input = new org.antlr.runtime.ANTLRStringStream(krl);
                com.kynetx.RuleSetLexer lexer = new com.kynetx.RuleSetLexer(input);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                com.kynetx.RuleSetParser parser = new com.kynetx.RuleSetParser(tokens);
                parser.ruleset();
                HashMap map = new HashMap();
                JSONObject js = new JSONObject(parser.rule_json);
                if (parser.parse_errors.size() > 0) {
                    ArrayList elist = new ArrayList();
                    for (int i = 0;i< parser.parse_errors.size(); i++) {
                        elist.add(parser.parse_errors.get(i));
                    }
                    map.put("error",elist);
                    JSONObject error = new JSONObject(map);
                    return error.toString();
                }
                //return unescapeUnicode(js.toString());
                return js.toString();
            } catch(NoViableAltException nvA) {
                StringBuffer errorText = new StringBuffer();
                errorText.append(nvA.toString());
                org.antlr.runtime.Token t = nvA.token;
                errorText.append("[").append(t.getLine()).append(":").append(t.getCharPositionInLine()).append("]");
                errorText.append(" near \"").append(t.getText()).append("\"");
                HashMap map = new HashMap();
                ArrayList elist = new ArrayList();
                elist.add(errorText.toString());
                map.put("error",elist);
                JSONObject error = new JSONObject(map);
                return error.toString();                            
            } catch(Exception e) {
                StringBuffer sb = new StringBuffer();
                sb.append("Parser Exception (" + e.getMessage() + "): ");
                sb.append(e.getStackTrace()[0].getClassName()).append(":");
                sb.append(e.getStackTrace()[0].getMethodName()).append(":");
                sb.append(e.getStackTrace()[0].getLineNumber()).append("::");
		            HashMap map = new HashMap();
		            ArrayList elist = new ArrayList();
                elist.add(sb.toString());
                map.put("error",elist);
                JSONObject error = new JSONObject(map);
                return error.toString();
            }
        }

            public String meta_block(String krl) throws org.antlr.runtime.RecognitionException {
            try {
                org.antlr.runtime.ANTLRStringStream input = new org.antlr.runtime.ANTLRStringStream(krl);
                com.kynetx.RuleSetLexer lexer = new com.kynetx.RuleSetLexer(input);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                com.kynetx.RuleSetParser parser = new com.kynetx.RuleSetParser(tokens);
                parser.meta_block();
                HashMap result = (HashMap)parser.rule_json.get("meta");
                HashMap map = new HashMap();
                map.put("result", result);
                //JSONObject js = new JSONObject(map);
                //System.err.println("Java Secret Sauce: "  + js.toString() + "\n");
                if (parser.parse_errors.size() > 0) {
                    ArrayList elist = new ArrayList();
                    for (int i = 0;i< parser.parse_errors.size(); i++) {
                        elist.add(parser.parse_errors.get(i));
                    }
                    map.put("error",elist);
                }
                JSONObject js = new JSONObject(map);
                //return unescapeUnicode(js.toString());
                return js.toString();
            } catch(Exception e) {
                StringBuffer sb = new StringBuffer();
                sb.append("Parser Exception (" + e.getMessage() + "): ");
                sb.append(e.getStackTrace()[0].getClassName()).append(":");
                sb.append(e.getStackTrace()[0].getMethodName()).append(":");
                sb.append(e.getStackTrace()[0].getLineNumber()).append(":");
                return (sb.toString());
            }
        }

        public String expr(String krl) throws org.antlr.runtime.RecognitionException {
            try {
                org.antlr.runtime.ANTLRStringStream input = new org.antlr.runtime.ANTLRStringStream(krl);
                com.kynetx.RuleSetLexer lexer = new com.kynetx.RuleSetLexer(input);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                com.kynetx.RuleSetParser parser = new com.kynetx.RuleSetParser(tokens);
                com.kynetx.RuleSetParser.expr_return result = parser.expr();
                HashMap map = new HashMap();
                map.put("result",result.result);
                JSONObject js = new JSONObject(map);
                if (parser.parse_errors.size() > 0) {
                    ArrayList elist = new ArrayList();
                    for (int i = 0;i< parser.parse_errors.size(); i++) {
                        elist.add(parser.parse_errors.get(i));
                    }
                    map.put("error",elist);
                    JSONObject error = new JSONObject(map);
                    return error.toString();
                }
                //return unescapeUnicode(js.toString());
                return js.toString();
            } catch(Exception e) {
                StringBuffer sb = new StringBuffer();
                sb.append("Parser Exception (" + e.getMessage() + "): ");
                sb.append(e.getStackTrace()[0].getClassName()).append(":");
                sb.append(e.getStackTrace()[0].getMethodName()).append(":");
                sb.append(e.getStackTrace()[0].getLineNumber()).append(":");
                return (sb.toString());
            }
        }

        public String decl(String krl) throws org.antlr.runtime.RecognitionException {
            try {
                org.antlr.runtime.ANTLRStringStream input = new org.antlr.runtime.ANTLRStringStream(krl);
                com.kynetx.RuleSetLexer lexer = new com.kynetx.RuleSetLexer(input);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                com.kynetx.RuleSetParser parser = new com.kynetx.RuleSetParser(tokens);
                ArrayList block_array = new ArrayList();
                parser.decl(block_array);
                HashMap map = new HashMap();
                map.put("result", block_array.toArray()[0]);
                JSONObject js = new JSONObject(map);
                if (parser.parse_errors.size() > 0) {
                    ArrayList elist = new ArrayList();
                    for (int i = 0;i< parser.parse_errors.size(); i++) {
                        elist.add(parser.parse_errors.get(i));
                    }
                    map.put("error",elist);
                    JSONObject error = new JSONObject(map);
                    return error.toString();
                }
                //return unescapeUnicode(js.toString());
                return js.toString();
            } catch(Exception e) {
                StringBuffer sb = new StringBuffer();
                sb.append("Parser Exception (" + e.getMessage() + "): ");
                sb.append(e.getStackTrace()[0].getClassName()).append(":");
                sb.append(e.getStackTrace()[0].getMethodName()).append(":");
                sb.append(e.getStackTrace()[0].getLineNumber()).append(":");
                return (sb.toString());
            }
        }

        public String pre_block(String krl) throws org.antlr.runtime.RecognitionException {
            try {
                org.antlr.runtime.ANTLRStringStream input = new org.antlr.runtime.ANTLRStringStream(krl);
                com.kynetx.RuleSetLexer lexer = new com.kynetx.RuleSetLexer(input);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                com.kynetx.RuleSetParser parser = new com.kynetx.RuleSetParser(tokens);
                com.kynetx.RuleSetParser.pre_block_return result = parser.pre_block();
                HashMap map = new HashMap();
                map.put("result",result.result);
                JSONObject js = new JSONObject(map);
                if (parser.parse_errors.size() > 0) {
                    ArrayList elist = new ArrayList();
                    for (int i = 0;i< parser.parse_errors.size(); i++) {
                        elist.add(parser.parse_errors.get(i));
                    }
                    map.put("error",elist);
                    JSONObject error = new JSONObject(map);
                    return error.toString();
                }
                //return unescapeUnicode(js.toString());
                return js.toString();
            } catch(Exception e) {
                StringBuffer sb = new StringBuffer();
                sb.append("Parser Exception (" + e.getMessage() + "): ");
                sb.append(e.getStackTrace()[0].getClassName()).append(":");
                sb.append(e.getStackTrace()[0].getMethodName()).append(":");
                sb.append(e.getStackTrace()[0].getLineNumber()).append(":");
                return (sb.toString());
            }
        }

        public String global(String krl) throws org.antlr.runtime.RecognitionException {
            try {
                org.antlr.runtime.ANTLRStringStream input = new org.antlr.runtime.ANTLRStringStream(krl);
                com.kynetx.RuleSetLexer lexer = new com.kynetx.RuleSetLexer(input);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                com.kynetx.RuleSetParser parser = new com.kynetx.RuleSetParser(tokens);
                parser.global_block();
                ArrayList array = (ArrayList)parser.rule_json.get("global");
                HashMap map = new HashMap();
                map.put("result", array);
                JSONObject js = new JSONObject(map);
                if (parser.parse_errors.size() > 0) {
                    ArrayList elist = new ArrayList();
                    for (int i = 0;i< parser.parse_errors.size(); i++) {
                        elist.add(parser.parse_errors.get(i));
                    }
                    map.put("error",elist);
                    JSONObject error = new JSONObject(map);
                    return error.toString();
                }
                //return unescapeUnicode(js.toString());
                return js.toString();
            } catch(Exception e) {
                StringBuffer sb = new StringBuffer();
                sb.append("Parser Exception (" + e.getMessage() + "): ");
                sb.append(e.getStackTrace()[0].getClassName()).append(":");
                sb.append(e.getStackTrace()[0].getMethodName()).append(":");
                sb.append(e.getStackTrace()[0].getLineNumber()).append(":");
                return (sb.toString());
            }
        }

        public String rule(String krl) throws org.antlr.runtime.RecognitionException {
            try {
                org.antlr.runtime.ANTLRStringStream input = new org.antlr.runtime.ANTLRStringStream(krl);
                com.kynetx.RuleSetLexer lexer = new com.kynetx.RuleSetLexer(input);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                com.kynetx.RuleSetParser parser = new com.kynetx.RuleSetParser(tokens);
                parser.rule();
                ArrayList array = (ArrayList)parser.rule_json.get("rules");
                HashMap map = new HashMap();
                map.put("result", array.toArray()[0]);
                JSONObject js = new JSONObject(map);
                if (parser.parse_errors.size() > 0) {
                    ArrayList elist = new ArrayList();
                    for (int i = 0;i< parser.parse_errors.size(); i++) {
                        elist.add(parser.parse_errors.get(i));
                    }
                    map.put("error",elist);
                    JSONObject error = new JSONObject(map);
                    return error.toString();
                }
                //return unescapeUnicode(js.toString());
                return js.toString();
            } catch(Exception e) {
                return (exceptionMessage(e.getMessage()));
            }
        }

        public String action(String krl) throws org.antlr.runtime.RecognitionException {
            try {
                org.antlr.runtime.ANTLRStringStream input = new org.antlr.runtime.ANTLRStringStream(krl);
                com.kynetx.RuleSetLexer lexer = new com.kynetx.RuleSetLexer(input);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                com.kynetx.RuleSetParser parser = new com.kynetx.RuleSetParser(tokens);
                HashMap map = new HashMap();
                parser.action(map);
                JSONObject js = new JSONObject(map);
                if (parser.parse_errors.size() > 0) {
                    ArrayList elist = new ArrayList();
                    for (int i = 0;i< parser.parse_errors.size(); i++) {
                        elist.add(parser.parse_errors.get(i));
                    }
                    map.put("error",elist);
                    JSONObject error = new JSONObject(map);
                    return error.toString();
                }
                //return unescapeUnicode(js.toString());
                return js.toString();
            } catch(Exception e) {
                StringBuffer sb = new StringBuffer();
                sb.append("Parser Exception (" + e.getMessage() + "): ");
                sb.append(e.getStackTrace()[0].getClassName()).append(":");
                sb.append(e.getStackTrace()[0].getMethodName()).append(":");
                sb.append(e.getStackTrace()[0].getLineNumber()).append(":");
                return (sb.toString());
            }
        }

        public String callbacks(String krl) throws org.antlr.runtime.RecognitionException {
            try {
                org.antlr.runtime.ANTLRStringStream input = new org.antlr.runtime.ANTLRStringStream(krl);
                com.kynetx.RuleSetLexer lexer = new com.kynetx.RuleSetLexer(input);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                com.kynetx.RuleSetParser parser = new com.kynetx.RuleSetParser(tokens);
                com.kynetx.RuleSetParser.callbacks_return result = parser.callbacks();
                HashMap map = new HashMap();
                map.put("result",result.result);
                JSONObject js = new JSONObject(map);
                if (parser.parse_errors.size() > 0) {
                    ArrayList elist = new ArrayList();
                    for (int i = 0;i< parser.parse_errors.size(); i++) {
                        elist.add(parser.parse_errors.get(i));
                    }
                    map.put("error",elist);
                    JSONObject error = new JSONObject(map);
                    return error.toString();
                }
                //return unescapeUnicode(js.toString());
                return js.toString();
            } catch(Exception e) {
                StringBuffer sb = new StringBuffer();
                sb.append("Parser Exception (" + e.getMessage() + "): ");
                sb.append(e.getStackTrace()[0].getClassName()).append(":");
                sb.append(e.getStackTrace()[0].getMethodName()).append(":");
                sb.append(e.getStackTrace()[0].getLineNumber()).append(":");
                return (sb.toString());
            }
        }

        public String post_block(String krl) throws org.antlr.runtime.RecognitionException {
            try {
                org.antlr.runtime.ANTLRStringStream input = new org.antlr.runtime.ANTLRStringStream(krl);
                com.kynetx.RuleSetLexer lexer = new com.kynetx.RuleSetLexer(input);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                com.kynetx.RuleSetParser parser = new com.kynetx.RuleSetParser(tokens);
                com.kynetx.RuleSetParser.post_block_return result = parser.post_block();
                HashMap map = new HashMap();
                map.put("result",result.result);
                JSONObject js = new JSONObject(map);
                if (parser.parse_errors.size() > 0) {
                    ArrayList elist = new ArrayList();
                    for (int i = 0;i< parser.parse_errors.size(); i++) {
                        elist.add(parser.parse_errors.get(i));
                    }
                    map.put("error",elist);
                    JSONObject error = new JSONObject(map);
                    return error.toString();
                }
                //return unescapeUnicode(js.toString());
                return js.toString();
            } catch(Exception e) {
                StringBuffer sb = new StringBuffer();
                sb.append("Parser Exception (" + e.getMessage() + "): ");
                sb.append(e.getStackTrace()[0].getClassName()).append(":");
                sb.append(e.getStackTrace()[0].getMethodName()).append(":");
                sb.append(e.getStackTrace()[0].getLineNumber()).append(":");
                return (sb.toString());
            }
        }

            public String dispatch_block(String krl) throws org.antlr.runtime.RecognitionException {
            try {
                org.antlr.runtime.ANTLRStringStream input = new org.antlr.runtime.ANTLRStringStream(krl);
                com.kynetx.RuleSetLexer lexer = new com.kynetx.RuleSetLexer(input);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                com.kynetx.RuleSetParser parser = new com.kynetx.RuleSetParser(tokens);
                parser.dispatch_block();
                ArrayList array = (ArrayList)parser.rule_json.get("dispatch");
                HashMap map = new HashMap();
                map.put("result", array);
                JSONObject js = new JSONObject(map);
                if (parser.parse_errors.size() > 0) {
                    ArrayList elist = new ArrayList();
                    for (int i = 0;i< parser.parse_errors.size(); i++) {
                        elist.add(parser.parse_errors.get(i));
                    }
                    map.put("error",elist);
                    JSONObject error = new JSONObject(map);
                    return error.toString();
                }
                //return unescapeUnicode(js.toString());
                return js.toString();
            } catch(Exception e) {
                StringBuffer sb = new StringBuffer();
                sb.append("Parser Exception (" + e.getMessage() + "): ");
                sb.append(e.getStackTrace()[0].getClassName()).append(":");
                sb.append(e.getStackTrace()[0].getMethodName()).append(":");
                sb.append(e.getStackTrace()[0].getLineNumber()).append(":");
                return (sb.toString());
            }
        }

        public String exceptionMessage(String s) {
            StackTraceElement stackTraceElements[] =
                (new Throwable()).getStackTrace();
            StringBuffer sb = new StringBuffer();
            sb.append("{\"error\":[\"");
            sb.append("Parser Exception (" + s + "): ");
            sb.append(stackTraceElements[1].toString()).append("\"]}");
            return (sb.toString());
        }
    }
