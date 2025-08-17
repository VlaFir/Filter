import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;


public class Filter {
    public static String wayOut = System.getProperty("user.dir") + "\\";
    public static String fileInt = "integers.txt";
    public static String fileFloat = "float.txt";
    public static String fileString = "string.txt";
    public static long intMin = Integer.MAX_VALUE;
    public static long intMax = Integer.MIN_VALUE;
    public static long intSum = 0;
    public static int intCount = 0;
    public static float floatMin = Float.MAX_VALUE;
    public static float floatMax = Float.MIN_VALUE;
    public static float floatSum = 0;
    public static int floatCount = 0;
    public static int stringMin = Integer.MAX_VALUE;
    public static int stringMax = Integer.MIN_VALUE;

    public static void main(String[] args) throws IOException {
        boolean appToFile = false;
        boolean shortStat = false;
        boolean fullStat = false;
        ArrayList <String> fileNames = new ArrayList<>();
        for( int i = 0; i < args.length; i++){
            String aString = args[i];
            switch (args[i]){
                case "-a": //Отмена перезаписи
                appToFile = true;
                break;

                case "-s": //Краткая стата
                shortStat = true;
                break;

                case "-f": //Полная стата
                fullStat = true;
                break;

                case "-p": //Префикс
                  if (i + 1 < args.length) {
                        if(args[i+1].endsWith("_") || args[i+1].endsWith("-") || args[i+1].endsWith(".")){
                            String prefix = args[i+1];
                            fileInt = prefix + fileInt;
                            fileFloat = prefix + fileFloat; 
                            fileString = prefix + fileString;
                            i++;
                        }
                        else{
                            System.err.println("Ошибка: неверно указан разделитель (Прим.: - _ .). Попробуйте ещё раз.");
                            return;
                        }
                    } else {
                        System.err.println("Ошибка: после -p не указан префикс. Попробуйте ещё раз.");
                        return;
                    }
                break;

                case "-o": //Путь
                if (i + 1 < args.length){
                    if (args[i+1].matches("^(?:[a-zA-Z]:)?(?:\\\\[^\\\\]+)+\\\\?$")) {
                        wayOut = args[i+1] + "\\";
                        i++;
                    }
                    else{
                        System.err.println("Ошибка: путь указан не верно. Попробуйте ещё раз.");
                        return;
                    }
                }
                else{
                    System.err.println("Ошибка: после -o не указан путь. Попробуйте ещё раз.");
                    return;
                }
                break;
                
                default:
                if(aString.endsWith(".txt")){
                    fileNames.add(aString);
                }
                else {
                    System.err.println("Ошибка: неизвестная опция");
                    return;
                }
                break;
            }
        }

        Files.createDirectories(Paths.get(wayOut));
        
        if(fileNames.isEmpty()){
            System.err.println("Ошибка: не указаны входные файлы. Попробуйте ещё раз.");
            return;
        }

        if(appToFile == false){
        fileClear(wayOut, fileInt);
        fileClear(wayOut, fileFloat);
        fileClear(wayOut, fileString);
        }

        int countLineAll = 0;

        for(int i = 0; i < fileNames.size(); i++){
            try (Scanner scanner = new Scanner(new File(fileNames.get(i)), StandardCharsets.UTF_8 )) {
                
                while(scanner.hasNextLine()){
                    String line = (String) scanner.nextLine();
                    lineType(line);
                    countLineAll++;
                }
            }
            catch(IOException e){
            System.err.println("Ошибка: неудалось прочесть входные файлы. " + e.getMessage());
            }
        }
        if(shortStat == true){ //Краткая стата
            System.out.println("Всего записанных строк: " + countLineAll);
            shortStatistic(wayOut, fileInt);
            shortStatistic(wayOut, fileFloat);
            shortStatistic(wayOut, fileString);
        }
        if(fullStat == true){ //Полная стата
            System.out.println("Всего записанных строк: " + countLineAll + "\n");
            fullStatistic(wayOut,fileInt);
            fullStatistic(wayOut,fileFloat);
            fullStatistic(wayOut,fileString);
        }
        
    }
        
    public static void lineType (String input) throws IOException {
        String lineSeparator = System.lineSeparator();
        // Int
        if (input.matches("^-?\\d+$")) {
            //Сбор статистики
            long num = Long.parseLong(input);
            intMin = Math.min(intMin, num);
            intMax = Math.max(intMax, num);
            intSum += num;
            intCount++;
            //Запись строки в файл
            fileWriters(wayOut, fileInt, input, lineSeparator);
        }

        // Float
        else if (input.matches("^[+-]?((\\d+\\.?\\d*)|(\\.\\d+))([eE][+-]?\\d+)?[fF]?$")) {
            //Сбор статистики
            float num = Float.parseFloat(input);
            floatMin = Math.min(floatMin, num);
            floatMax = Math.max(floatMax, num);
            floatSum += num;
            floatCount++;
            //Запись строки в файл
            fileWriters(wayOut, fileFloat, input, lineSeparator);
        }

        // String
        else{
            //Сбор статистики
            int length = input.length();
            stringMin = Math.min(stringMin, length);
            stringMax = Math.max(stringMax, length);
            //Запись строки в файл
            fileWriters(wayOut, fileString, input, lineSeparator);
        }
        
        
    }
    
    //Чистит файлы перед записью 
    public static void fileClear (String way, String file) throws IOException{ 
        try (FileWriter fileWriter = new FileWriter(way + file, StandardCharsets.UTF_8, false)) {
            fileWriter.write("");
        }
        catch(IOException e){
            System.err.println("Ошибка: неудалось очистить файл " + file + ". " + e.getMessage());
        }
    }

    //Метод сбора короткой статы с файлов
    public static void shortStatistic (String way, String file) throws IOException {
        try {
            long countLine = Files.lines(Paths.get(way + file)).count();
            System.out.println("Всего записанных строк в файл " + file + ": " + countLine);
        }
        catch(IOException e){
            System.err.println("Ошибка: неудалось собрать статистику с файла: " + file + ". " + e.getMessage());
        }
    }

    //Метод записи в файл
    public static void fileWriters (String way, String file, String input, String lineSeparator) throws IOException{ 
        try(FileWriter fileWriter = new FileWriter(way + file, StandardCharsets.UTF_8, true)){
            fileWriter.write(input + lineSeparator);
        }
        catch(IOException e){
            System.err.println("Ошибка: неудалось записать в файл " + file + ". " + e.getMessage());
        }
    }

    public static void fullStatistic (String way, String file) throws IOException {
        System.out.println("Статистика по файлу: " + file);
        try {
            long countLine = Files.lines(Paths.get(way + file)).count();
            System.out.println("Всего записанных строк в файл " + file + ": " + countLine);
        }
        catch(IOException e){
            System.err.println("Ошибка: неудалось собрать статистику с файла: " + file + ". " + e.getMessage());
        }
        
        // INT
        if(file.endsWith("integers.txt")){
            System.out.println("Минимальное значение:" + intMin);
            System.out.println("Максимальное значение: " + intMax);
            System.out.println("Сумма всех значений: " + intSum);
            System.out.println("Среднее значение: " + (float) intSum / intCount + "\n");
        }
        

        // FLOAT
        if(file.endsWith("float.txt")){
            System.out.println("Минимальное значение:" + floatMin);
            System.out.println("Максимальное значение: " + floatMax);
            System.out.println("Сумма всех значений: " + floatSum);
            System.out.println("Среднее значение: " + floatSum / floatCount + "\n");
        }
        

        // STRING
        if(file.endsWith("string.txt")){
            System.out.println("Количество символов в самой короткой строке: " + stringMin);
            System.out.println("Количество символов в самой длинной строке: " + stringMax);
        }
    }
}

    

    

    

