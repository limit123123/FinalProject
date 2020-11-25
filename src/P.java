//Programmer：Sisi Kang
//11/24/2020

import jm.music.data.Score;
import jm.util.Play;
import jm.util.Read;
import processing.core.PApplet;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

public class P extends PApplet {
	MelodyPlayer player = new MelodyPlayer(this, 100.0f);
	//Minim minim;
	int mode = 0;
	String filePath = null;
	public static void main(String[] args) {
		PApplet.main("P"); 
	}
	public void settings() {
		size(850,450);
		ArrayList<Integer> bgm = new ArrayList<>();
		bgm.add(0);
		player.setup();
		player.setMelody(bgm);
	}

	public	void draw(){
		fill(255,255,255);
		rect(100,100,100,300);
		rect(200,100,100,300);
		rect(300,100,100,300);
		rect(400,100,100,300);
		rect(500,100,200,300);
		//3 rect 
		fill(0,0,0);
		rect(175,100,50,200);
		rect(275,100,50,200);
		rect(375,100,50,200);
		fill(255,255,255);
		//4 big cirlce
		fill(90,89,87);
		ellipse(550,150,50,50);
//		ellipse(650,150,50,50);
		ellipse(550,250,50,50);
		ellipse(650,250,50,50);
		////4 small cirlce
		fill(145,201,64);
		ellipse(550,150,25,25);
		ellipse(550,250,25,25);
		ellipse(650,250,25,25);
//		fill(145,201,64);
//		ellipse(650,150,25,25);
		// word a s d f
		  textSize(50);
		fill(44,36,34);
		text("A", 135, 360);
		text("S", 235, 360);
		text("D", 335, 360);
		text("F", 435, 360);
		// word W E R
		  textSize(40);
		fill(255,255,255);
		text("W", 183, 250);
		text("E", 288, 250);
		text("R", 387, 250);
		// word melody markov Beats mc 
		  textSize(20);
		fill(44,36,34);
		text("o- Orig. Melody", 517, 195);
		text("m- Markov", 505,295);
		text("p- PST", 625, 295);
		// word AI.2.0
		 textSize(20);
			fill(174,165,156);
			text("AI.2.0", 630, 385);
			// word ARTIFICIAL IMPROVISER 2.0
			 textSize(25);
				fill(44,30,62);
				text("ARTIFICIAL IMPROVISER 2.0", 200, 70);
				

				player.play();
		  
		}
	String getPath(String path) {

		String filePath = "";
		try {
			filePath = URLDecoder.decode(getClass().getResource(path).getPath(), "UTF-8");

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath;
	}
	void playMidiFile(String filename) {
		Score theScore = new Score("Temporary score");
		Read.midi(theScore, filename);
		Play.midi(theScore);
	}
	public void keyPressed() {
		MidiFileToNotes midiNotesMary;
		if (key == 'a') {
			filePath = getPath("mid/Midi1A.mp3 (1).mid");
		}
		else if (key == 's') {
			filePath = getPath("mid/Midi2F.mp3.mid");
		}
		else if (key == 'd') {
			filePath = getPath("mid/midi3F.mp3.mid");
		}
		else if (key == 'f') {
			filePath = getPath("mid/Midi4B.mp3.mid");
		}
		else if (key == 'w') {
			filePath = getPath("mid/midi5Fs.mp3.mid");
		}
		else if (key == 'e') {
			filePath = getPath("mid/midi6Gs.mp3.mid");
			}
		else if (key == 'r') {
			filePath = getPath("mid/midi7As.mp3.mid");
		} else if (key == 'o') {
			mode = 0;
		} else if (key == 'm') {
			mode = 1;
		} else if (key == 'p') {
			mode = 2;
		}

		if (filePath == null) {
			System.out.println("Choose path first!!!");
			return;
		}
		midiNotesMary = new MidiFileToNotes(filePath);
		midiNotesMary.setWhichLine(0);

		ArrayList<Integer> new_pitch = new ArrayList<>();
		ArrayList<Double> new_rhythm = new ArrayList<>();
		if (mode == 0) {
			playMidiFile(filePath);
			return;
		} else if (mode == 1) {
			MarkovGenerator<Integer> melodyGen_pitch  = new MarkovGenerator<>(1);
			ProbabilityGenerator<Integer> firstNoteGen_pitch = new ProbabilityGenerator<>();
			MarkovGenerator<Double> melodyGen_rhythm  = new MarkovGenerator<>(1);
			ProbabilityGenerator<Double> firstNoteGen_rhythm = new ProbabilityGenerator<>();

			firstNoteGen_pitch.train(midiNotesMary.getPitchArray());
			melodyGen_pitch.train(midiNotesMary.getPitchArray());
			firstNoteGen_rhythm.train(midiNotesMary.getRhythmArray());
			melodyGen_rhythm.train(midiNotesMary.getRhythmArray());
			melodyGen_pitch.e = firstNoteGen_pitch;
			melodyGen_rhythm.e = firstNoteGen_rhythm;

			for (int i=0; i<midiNotesMary.getPitchArray().size()/20; i++) {
				ArrayList<Integer> initSeq_pitch = new ArrayList<>();
				ArrayList<Double> initSeq_rhythm = new ArrayList<>();
				initSeq_pitch.add(firstNoteGen_pitch.generate());
				initSeq_rhythm.add(firstNoteGen_rhythm.generate());
				new_pitch.addAll(melodyGen_pitch.generate(20, initSeq_pitch));
				new_rhythm.addAll(melodyGen_rhythm.generate(20, initSeq_rhythm));
			}
		} else if (mode == 2) {
			Tree<Integer> suffix_pitch = new Tree<>(3,0.01,1.5);
			suffix_pitch.train(midiNotesMary.getPitchArray());
			Tree<Double> suffix_rhythm = new Tree<>(3,0.01,1.5);
			suffix_rhythm.train(midiNotesMary.getRhythmArray());

			suffix_pitch.print();
			suffix_rhythm.print();
			new_pitch = suffix_pitch.generate(midiNotesMary.getPitchArray().size());
			new_rhythm = suffix_rhythm.generate(midiNotesMary.getRhythmArray().size());
		}
		System.out.println("Here follows the new melody:");
		System.out.println(new_pitch);
		System.out.println(new_rhythm);

		player.setMelody(new_pitch); //assignments, generating probability on each pitch
		player.setRhythm(new_rhythm); //assignments, generating probability on each rhythm
		player.play();
	}
}