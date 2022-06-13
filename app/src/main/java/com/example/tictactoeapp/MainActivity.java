package com.example.tictactoeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // tạo biến
    private TextView playerOneScore, playerTwoScore, playerStatus;
    private Button [] buttons = new Button[9];
    private Button resetGame;

    private int playerOneScoreCount, playerTwoScoreCount, rountCount;
    boolean activePlayer;

    // p1 => 0
    // p2 => 1
    // empty => 2
    int [] gameState = {2,2,2,2,2,2,2,2,2};

    // vị trí chiến thắng
    int [][] winningPositions = {
            {0,1,2}, {3,4,5}, {6,7,8}, // row
            {0,3,6}, {1,4,7}, {2,5,8}, // columns
            {0,4,8}, {2,4,6} // cross
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // onCreate(): Đây là hàm bắt buộc phải được chạy, được gọi đầu tiên một lần duy nhất khi hệ thống khởi tạo Activity
        super.onCreate(savedInstanceState);
        // setContentView: nạp giao diện
        setContentView(R.layout.activity_main);

        // gán textView cho biến
        playerOneScore = (TextView) findViewById(R.id.playerOneScore);
        playerTwoScore = (TextView) findViewById(R.id.playerTwoScore);
        playerStatus = (TextView) findViewById(R.id.playerStatus);

        // gán nút reset game cho biến
        resetGame = (Button) findViewById(R.id.resetGame);

        // gán các bút cho mảng buttons
        for(int i = 0; i < buttons.length; i++) {
            String buttonID = "btn_" + i;
            int resourceID = getResources().getIdentifier(buttonID, "id", getPackageName());
            buttons[i] = (Button) findViewById(resourceID);
            buttons[i].setOnClickListener(this);
        }

        rountCount = 0;
        playerOneScoreCount = 0;
        playerTwoScoreCount = 0;
        activePlayer = true;

    }

    @Override
    public void onClick(View v) {
        if (!((Button)v).getText().toString().equals("")) {
            return;
        }
        String buttonID = v.getResources().getResourceEntryName(v.getId()); //btn_2
        int gameStatePointer = Integer.parseInt(buttonID.substring(buttonID.length() - 1, buttonID.length()));

        // kiểm tra xem đến lượt ai
        if (activePlayer) {
            ((Button) v).setText("X");
            ((Button) v).setTextColor(Color.parseColor("#FFC34A"));
            gameState[gameStatePointer] = 0;
        } else {
            ((Button) v).setText("O");
            ((Button) v).setTextColor(Color.parseColor("#70FFEA"));
            gameState[gameStatePointer] = 1;
        }
        rountCount++;

        // kiểm tra xem ai win
        if (checkWinner()) {
            if (activePlayer) {
                playerOneScoreCount++;
                updatePlayerScore();
                Toast.makeText(this, "Player One Won!", Toast.LENGTH_SHORT).show();
                playAgain();
            } else {
                playerTwoScoreCount++;
                updatePlayerScore();
                Toast.makeText(this, "Player Two Won!", Toast.LENGTH_SHORT).show();
                playAgain();
            }
            // nếu 2 người chơi đã chọn hết ô thì chơi lại
        } else if (rountCount == 9) {
            playAgain();
            //Toast.makeText(this, "No Winner!", Toast.LENGTH_SHORT).show();
        // những trường hợp còn lại
        } else {
            activePlayer = !activePlayer;
        }

        // hiển thị người đang thắng ra màn hình
        if (playerOneScoreCount > playerTwoScoreCount) {
            playerStatus.setText("Player One is Winning!");
        } else if (playerTwoScoreCount > playerOneScoreCount) {
            playerStatus.setText("Player Two is Winning!");
        } else {
            playerStatus.setText("");
        }

        // click btn reset game
        resetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAgain();
                playerOneScoreCount = 0;
                playerTwoScoreCount = 0;
                playerStatus.setText("");
                updatePlayerScore();
            }
        });
    }

    // check winner
    public boolean checkWinner() {
        boolean winnerResult = false;

        for (int [] winningPosion : winningPositions) {
            if (gameState[winningPosion[0]] == gameState[winningPosion[1]] &&
                    gameState[winningPosion[1]] == gameState[winningPosion[2]] &&
                    gameState[winningPosion[0]] != 2) {
                winnerResult = true;
            }
        }

        return winnerResult;
    }

    // update tỉ số
    public void updatePlayerScore() {
        playerOneScore.setText(Integer.toString(playerOneScoreCount));
        playerTwoScore.setText(Integer.toString(playerTwoScoreCount));
    }

    // game được chơi lại
    public void playAgain() {
        rountCount = 0;
        activePlayer = true;

        for (int i = 0; i < buttons.length; i++) {
            gameState[i] = 2;
            buttons[i].setText("");
        }
    }
}