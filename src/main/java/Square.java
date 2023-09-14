import javax.swing.*;
import java.awt.*;

public class Square extends JButton {


    public Square(Window window, int row, int column) {
        super();
        setFont(Constants.FONT);
        setBackground(Color.WHITE);
        setFocusPainted(false);
        setPreferredSize(new Dimension(Constants.SQUARE_SIZE, Constants.SQUARE_SIZE));
        addActionListener(e -> {
            Integer rivalMove = ApiUtil.markSquare(window.convertSquaresToMatrix(), row, column);
            this.mark(true);
            if (rivalMove != null) {
                window.enableDisableEmptySquares(false);
                Square square =  window.getSquare(rivalMove);
                new Thread(() -> {
                    Utils.sleep(Constants.DELAY_BEFORE_RIVAL_TURN);
                    SwingUtilities.invokeLater(() -> {
                        square.mark(false);
                        window.enableDisableEmptySquares(true);
                    });
                }).start();
            }
        });
    }

    public void mark (boolean player) {
        this.setBackground(player ? Color.BLUE : Color.BLACK);
        this.setEnabled(false);
    }

}
