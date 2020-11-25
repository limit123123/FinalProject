
/*
//Programmer：Sisi Kang
//11/24/2020
 */

import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Read;

import java.util.ArrayList;

public class MidiFileToNotes {
	String filename;
	ArrayList<Integer> pitches;
	ArrayList<Double> rhythms;

	int whichLine;

	ArrayList<Note> melody;

	MidiFileToNotes(String f) {
		filename = f;
		processPitchesAsTokens();
		whichLine = 0;
	}

	void setWhichLine(int line) {
		whichLine = line;
	}

	void processPitchesAsTokens() {
		pitches = new ArrayList<Integer>();
		melody = new ArrayList<Note>();
		rhythms = new ArrayList<Double>();

		String scoreName = "score_" + filename;
		Score theScore = new Score(scoreName);

		// read the midi file into a score
		Read.midi(theScore, filename);

		// extract the melody and all its parts
		Part part = theScore.getPart(whichLine);
		Phrase[] phrases = part.getPhraseArray();

		// extract all the pitches and notes from the melody
		for (int i = 0; i < phrases.length; i++) {
			jm.music.data.Note[] notes = phrases[i].getNoteArray();

			for (int j = 0; j < notes.length; j++) {
				pitches.add(notes[j].getPitch());
				rhythms.add(notes[j].getDuration());
				melody.add(notes[j]);
			}

		}
	}

	public Integer[] getPitches() {
		return pitches.toArray(new Integer[pitches.size()]);
	}

	public ArrayList<Integer> getPitchArray() {
		return pitches;
	}

	public ArrayList<Double> getRhythmArray() {
		return rhythms;
	}

	public ArrayList<Note> getMelody() {
		return melody;
	}

	public Double[] getRhythms() {
		return rhythms.toArray(new Double[rhythms.size()]);
	}

}