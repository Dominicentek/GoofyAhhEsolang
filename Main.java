import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static int soundsPlaying = 0;
    public static void main(String[] args) throws Exception {
        if (args.length == 0) throw new RuntimeException("No file specified");
        Stack<Integer> stack = new Stack<>();
        ArrayList<String> instr = new ArrayList<>();
        Scanner scanner = new Scanner(new File(args[0]));
        while (scanner.hasNext()) {
            instr.add(scanner.nextLine().toLowerCase().replaceAll("\r", ""));
        }
        scanner.close();
        int i = 0;
        try {
            for (i = 0; i < instr.size(); i++) {
                String line = instr.get(i);
                if (line.equals("if")) {
                    playSound("huh");
                    if (stack.size() < 1) throw new RuntimeException("No data to compare");
                    if (stack.pop() == 0) i++;
                }
                else if (line.startsWith("goto")) {
                    playSound("slip");
                    i = Integer.parseInt(line.split(" ")[1]) - 2;
                }
                else if (line.equals("printstr")) {
                    playSound("fard");
                    if (stack.size() < 1) throw new RuntimeException("No data to print");
                    System.out.print((char)(int)stack.pop());
                }
                else if (line.equals("printnum")) {
                    playSound("nerd");
                    if (stack.size() < 1) throw new RuntimeException("No data to print");
                    System.out.print(stack.pop());
                }
                else if (line.equals("copy")) {
                    playSound("piano");
                    if (stack.size() < 1) throw new RuntimeException("No data to copy");
                    stack.push(stack.peek());
                }
                else if (line.equals("pop")) {
                    playSound("vine boom");
                    if (stack.size() < 1) throw new RuntimeException("No data to pop");
                    stack.pop();
                }
                else if (line.equals("add")) {
                    playSound("raaar");
                    if (stack.size() < 2) throw new RuntimeException("No data to perform addition");
                    int a = stack.pop();
                    int b = stack.pop();
                    stack.push(b + a);
                }
                else if (line.equals("subtract")) {
                    playSound("dry bones");
                    if (stack.size() < 2) throw new RuntimeException("No data to perform subtraction");
                    int a = stack.pop();
                    int b = stack.pop();
                    stack.push(b - a);
                }
                else if (line.equals("multiply")) {
                    playSound("bell");
                    if (stack.size() < 2) throw new RuntimeException("No data to perform multiplication");
                    int a = stack.pop();
                    int b = stack.pop();
                    stack.push(b * a);
                }
                else if (line.equals("divide")) {
                    playSound("omg");
                    if (stack.size() < 2) throw new RuntimeException("No data to perform division");
                    int a = stack.pop();
                    int b = stack.pop();
                    stack.push(b / a);
                }
                else if (line.equals("reverse")) {
                    playSound("boing");
                    Collections.reverse(stack);
                }
                else {
                    int num;
                    try {
                        num = Integer.parseInt(line);
                    }
                    catch (NumberFormatException e) {
                        throw new RuntimeException("Invalid instruction");
                    }
                    if (num >= 0 && num <= 9) {
                        if (num == 0) playSound("alarm");
                        if (num == 1) playSound("what the hell");
                        if (num == 2) playSound("aw hell nah");
                        if (num == 3) playSound("notification");
                        if (num == 4) playSound("21");
                        if (num == 5) playSound("laugh");
                        if (num == 6) playSound("pizza here");
                        if (num == 7) playSound("pablo");
                        if (num == 8) playSound("metal pipe");
                        if (num == 9) playSound("saul nokia");
                        stack.push(num);
                    }
                    else throw new RuntimeException("Invalid instruction");
                }
                Thread.sleep(500);
            }
        }
        catch (Exception e) {
            Thread.sleep(500);
            playSound("u stupid");
            System.err.println((e instanceof NumberFormatException ? "Malformed number" : e.getMessage()) + " (line " + (i + 1) + ")");
        }
        while (soundsPlaying > 0) {
            Thread.sleep(250);
        }
    }
    public static void playSound(String name) throws Exception {
        Clip clip = AudioSystem.getClip();
        clip.open(AudioSystem.getAudioInputStream(new BufferedInputStream(Main.class.getResourceAsStream("/sounds/" + name + ".wav"))));
        clip.addLineListener((e) -> {
            if (e.getType() == LineEvent.Type.STOP) {
                soundsPlaying--;
                clip.close();
            }
        });
        clip.start();
        soundsPlaying++;
    }
}