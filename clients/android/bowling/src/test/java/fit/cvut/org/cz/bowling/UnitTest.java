package fit.cvut.org.cz.bowling;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.business.entities.FrameOverview;
import fit.cvut.org.cz.bowling.presentation.constraints.ConstraintsConstants;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;


public class UnitTest {

    protected static List<FrameOverview> frameOverviews = new ArrayList<>();
    final static int maxFrameScore = ConstraintsConstants.tenPinFrameMaxScore;

    int currentFrameScore;

    List<Byte> rolls1 = new ArrayList<>();
    String playerName = "Player A";
    FrameOverview f1 = new FrameOverview((byte) 1, rolls1, playerName, 0, 1, 1, (byte) 0);

    List<Byte> rolls2 = new ArrayList<>();
    FrameOverview f2 = new FrameOverview((byte) 2, rolls2, playerName, 0, 1, 1, (byte) 10);

    List<Byte> rolls3 = new ArrayList<>();
    FrameOverview f3 = new FrameOverview((byte) 3, rolls3, playerName, 0, 1, 1, (byte) 10);

    List<Byte> rolls4 = new ArrayList<>();
    FrameOverview f4 = new FrameOverview((byte) 10, rolls4, playerName, 0, 1, 1, (byte) 20);


    public void add()
    {
            frameOverviews.add(f1);

        rolls2.add((byte) 10);
            frameOverviews.add(f2);

        rolls3.add((byte) 5);
        rolls3.add((byte) 5);
            frameOverviews.add(f3);

        rolls4.add((byte) 10);
        rolls4.add((byte) 6);
        rolls4.add((byte) 4);
            frameOverviews.add(f4);

    }

    private boolean updateScores(final int positionFrom) {
        int arraySize = frameOverviews.size();
        for(int i = positionFrom; i < arraySize; ++i) {
            FrameOverview f = frameOverviews.get(i);
            currentFrameScore = i == 0 ? 0 : frameOverviews.get(i-1).getCurrentScore();
            currentFrameScore += f.getFrameScore();
            if(f.getFrameScore() == maxFrameScore) {
                if(f.getRolls().get(0) == maxFrameScore){
                    currentFrameScore += getNextRollsScore(i, 2);
                } else {
                    currentFrameScore += getNextRollsScore(i, 1);
                }
            }
            f.setCurrentScore(currentFrameScore);
        }
        return true;
    }

    private int getNextRollsScore(final int positionFrom, final int rollsNumber) {
        int rollsRemaining = rollsNumber;
        int arraySize = frameOverviews.size();
        int readPosition = positionFrom + 1;
        int score = 0;
        while (rollsRemaining > 0 && readPosition < arraySize) {
            List<Byte> frameRolls = frameOverviews.get(readPosition).getRolls();
            score += frameRolls.get(0);
            if(frameRolls.size() >= rollsRemaining) {
                if(rollsRemaining == 2) {
                    score += frameRolls.get(1);
                }
                rollsRemaining = 0;
            } else {
                --rollsRemaining;
                ++readPosition;
            }
        }
        return score;
    }


    @Test
    public void frame_overview()
    {

        assertTrue("is Empty",frameOverviews.isEmpty());
        add();
        assertFalse("is Empty",frameOverviews.isEmpty());

        assertEquals((frameOverviews.get(0)).getPlayerName(),f1.getPlayerName());
        assertEquals((frameOverviews.get(0)).getFrameNumber(),f1.getFrameNumber());
        assertEquals((frameOverviews.get(0)).getCurrentScore(),f1.getCurrentScore());
        assertEquals((frameOverviews.get(0)).getPlayerId(),f1.getPlayerId());
        assertEquals((frameOverviews.get(0)).getRolls(),f1.getRolls());

        assertEquals((frameOverviews.get(1)).getPlayerName(),f2.getPlayerName());
        assertEquals((frameOverviews.get(1)).getFrameNumber(),f2.getFrameNumber());
        assertEquals((frameOverviews.get(1)).getCurrentScore(),f2.getCurrentScore());
        assertEquals((frameOverviews.get(1)).getPlayerId(),f2.getPlayerId());
        assertEquals((frameOverviews.get(1)).getRolls(),f2.getRolls());

        assertEquals((frameOverviews.get(2)).getPlayerName(),f3.getPlayerName());
        assertEquals((frameOverviews.get(2)).getFrameNumber(),f3.getFrameNumber());
        assertEquals((frameOverviews.get(2)).getCurrentScore(),f3.getCurrentScore());
        assertEquals((frameOverviews.get(2)).getPlayerId(),f3.getPlayerId());
        assertEquals((frameOverviews.get(2)).getRolls(),f3.getRolls());

        assertEquals((frameOverviews.get(3)).getPlayerName(),f4.getPlayerName());
        assertEquals((frameOverviews.get(3)).getFrameNumber(),f4.getFrameNumber());
        assertEquals((frameOverviews.get(3)).getCurrentScore(),f4.getCurrentScore());
        assertEquals((frameOverviews.get(3)).getPlayerId(),f4.getPlayerId());
        assertEquals((frameOverviews.get(3)).getRolls(),f4.getRolls());

        currentFrameScore += getNextRollsScore(0, 2);
        assertEquals(currentFrameScore,15);
        currentFrameScore += getNextRollsScore(1, 2);
        assertEquals(currentFrameScore,25);
        currentFrameScore += getNextRollsScore(2, 2);
        assertEquals(currentFrameScore,41);
        currentFrameScore += getNextRollsScore(3, 2);
        assertEquals(currentFrameScore,41);

        assertTrue("update Scores",updateScores(0));
        assertEquals(currentFrameScore,60);
        assertTrue("update Scores",updateScores(0));
        assertEquals(currentFrameScore,60);
        assertTrue("update Scores",updateScores(2));
        assertEquals(currentFrameScore,60);

    }


}