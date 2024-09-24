package chess;

import java.time.LocalDate;
import java.util.List;

public class Game {
    private List<String[]> moveList;
    private String title;
    private LocalDate date;

    public Game(String title, List<String[]> moveList, LocalDate date) {
        this.moveList = moveList;
        this.title = title;
        this.date = date;
    }

    public List<String[]> getMoveList() {
        return moveList;
    }

    public void setMoveList(List<String[]> moveList) {
        this.moveList = moveList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
