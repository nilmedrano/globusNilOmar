import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.globusnilomar.R
import java.util.*

class JocActivity : AppCompatActivity() {

    lateinit var buttonArray: Array<Button>
    lateinit var PVP: Button
    lateinit var PVC: Button

    private var player1 = ArrayList<Int>()
    private var player2 = ArrayList<Int>()
    private var activePlayer = 1
    private var setPlayer = 1
    private var gameActive = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joc)

        buttonArray = arrayOf(
            findViewById(R.id.button1), findViewById(R.id.button2), findViewById(R.id.button3),
            findViewById(R.id.button4), findViewById(R.id.button5), findViewById(R.id.button6),
            findViewById(R.id.button7), findViewById(R.id.button8), findViewById(R.id.button9)
        )
        PVP = findViewById(R.id.PVP)
        PVC = findViewById(R.id.PVC)
        setButtonActive(PVP)
        setButtonInactive(PVC)
    }

    private fun setButtonActive(button: Button) {
        button.setTextColor(ContextCompat.getColor(this, R.color.bluesoft))
        button.setBackgroundColor(ContextCompat.getColor(this, R.color.blueberry))
    }

    private fun setButtonInactive(button: Button) {
        button.setTextColor(ContextCompat.getColor(this, R.color.blueberry))
        button.setBackgroundColor(ContextCompat.getColor(this, R.color.bluesoft))
    }

    fun restartGame(view: View) {
        player1.clear()
        player2.clear()
        activePlayer = 1
        gameActive = true

        buttonArray.forEach { button ->
            button.text = ""
            button.setBackgroundColor(Color.TRANSPARENT)
            button.isEnabled = true
        }
    }

    fun buttonClick(view: View) {
        if (!gameActive) return

        val buttonClicked = view as Button
        var cellId = 0
        when (buttonClicked.id) {
            R.id.button1 -> cellId = 1
            R.id.button2 -> cellId = 2
            R.id.button3 -> cellId = 3
            R.id.button4 -> cellId = 4
            R.id.button5 -> cellId = 5
            R.id.button6 -> cellId = 6
            R.id.button7 -> cellId = 7
            R.id.button8 -> cellId = 8
            R.id.button9 -> cellId = 9
        }
        playGame(cellId, buttonClicked)
    }

    fun playerChoose(view: View) {
        val ps = view as Button
        when (ps.id) {
            R.id.PVP -> {
                setPlayer = 1
            }
            R.id.PVC -> {
                setPlayer = 2
                Handler().postDelayed({
                    autoPlay()
                }, 1000) // Espera de 1 segundo antes de que la CPU realice su movimiento
            }
        }
    }

    private fun playGame(cellId: Int, buttonClicked: Button) {
        if (!gameActive) return

        if (activePlayer == 1) {
            buttonClicked.text = "X"
            buttonClicked.setBackgroundColor(Color.GREEN)
            player1.add(cellId)
            activePlayer = 2
        } else {
            buttonClicked.text = "O"
            buttonClicked.setBackgroundColor(Color.CYAN)
            player2.add(cellId)
            activePlayer = 1
        }
        buttonClicked.isEnabled = false
        checkWinner()
    }

    private fun checkWinner() {
        val winningConditions = arrayOf(
            intArrayOf(1, 2, 3), intArrayOf(4, 5, 6), intArrayOf(7, 8, 9),
            intArrayOf(1, 4, 7), intArrayOf(2, 5, 8), intArrayOf(3, 6, 9),
            intArrayOf(1, 5, 9), intArrayOf(3, 5, 7)
        )

        for (condition in winningConditions) {
            val conditionList = condition.toList()
            if (player1.containsAll(conditionList)) {
                announceWinner(1)
                return
            } else if (player2.containsAll(conditionList)) {
                announceWinner(2)
                return
            }
        }

        if (player1.size + player2.size == 9) {
            announceWinner(0)
        }
    }


    private fun announceWinner(winner: Int) {
        gameActive = false

        when (winner) {
            1 -> showToast("Player 1 Wins!!")
            2 -> showToast(if (setPlayer == 1) "Player 2 Wins!!" else "CPU Wins!!")
            else -> showToast("It's a Draw!!")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun autoPlay() {
        if (!gameActive) return

        val emptyCells = ArrayList<Int>()
        for (cellId in 1..9) {
            if (!(player1.contains(cellId) || player2.contains(cellId))) {
                emptyCells.add(cellId)
            }
        }

        if (emptyCells.isNotEmpty()) {
            val random = Random()
            val randomIndex = random.nextInt(emptyCells.size)
            val cellId = emptyCells[randomIndex]

            val buSelect = buttonArray[cellId - 1]
            playGame(cellId, buSelect)
        }
    }
}
