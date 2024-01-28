import com.example.game2048.data.source.Pref
import com.example.game2048.domain.AppRepository

class AppRepositoryImpl : AppRepository {
    companion object {
        @Volatile
        private lateinit var instance: AppRepository

        fun init() {
            if (!(::instance.isInitialized)) instance = AppRepositoryImpl()
        }

        fun getAppRepository(): AppRepository = instance
    }

    private val pref = Pref.getInstance()
    private val matrixSize = 4
    private val initialTileProbability = 0.1

    private var matrix = Array(matrixSize) { Array(matrixSize) { 0 } }
    private var score = 0
        set(value) {
            field = value
            pref.setLastScore(value)
            if(pref.getHighScore()<value){
                pref.setHighScore(value)
            }
        }


    init {
        initializeMatrix()
    }
    override fun checkLose(): Boolean {
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                if (matrix[i][j] == 0) {
                    return false
                }
            }
        }

        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                if (j < matrixSize - 1 && matrix[i][j] == matrix[i][j + 1]) {
                    return false
                }
                if (i < matrixSize - 1 && matrix[i][j] == matrix[i + 1][j]) {
                    return false
                }
            }
        }

        return true
    }

    override fun loadLast(){
        matrix = pref.loadLast()
    }

    override fun saveLast() {
        return pref.saveLast(matrix)
    }

    override fun saveNothing() {
        pref.saveLast(Array(matrixSize) { Array(matrixSize) { 0 } })
    }

    override fun initializeMatrix() {
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                matrix[i][j] = 0
            }
        }
        score = 0
        addRandomTile()
        addRandomTile()
    }

    private fun addRandomTile() {
        val emptyPositions = mutableListOf<Pair<Int, Int>>()
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                if (matrix[i][j] == 0) {
                    emptyPositions.add(Pair(i, j))
                }
            }
        }

        if (emptyPositions.isNotEmpty()) {
            val randomPosition = emptyPositions.random()
            matrix[randomPosition.first][randomPosition.second] =
                if (Math.random() < initialTileProbability) 4 else 2
        }
    }

    override fun getMatrix(): Array<Array<Int>> {
        return matrix
    }

    override fun getScore(): Int {
        return score
    }
    override fun getHighScore(): Int {
        return pref.getHighScore()
    }

    override fun moveUp() {
        var moved = false

        for (j in matrix[0].indices) {
            var lastMergedPosition = -1

            for (i in 1 until matrixSize) {
                if (matrix[i][j] != 0) {
                    var targetPosition = i - 1

                    while (targetPosition >= 0 && matrix[targetPosition][j] == 0) {
                        matrix[targetPosition][j] = matrix[targetPosition + 1][j]
                        matrix[targetPosition + 1][j] = 0
                        targetPosition--
                        moved = true
                    }

                    if (targetPosition >= 0 && matrix[targetPosition][j] == matrix[targetPosition + 1][j]
                        && lastMergedPosition != targetPosition
                    ) {
                        matrix[targetPosition][j] *= 2
                        score += matrix[targetPosition][j]
                        matrix[targetPosition + 1][j] = 0
                        lastMergedPosition = targetPosition
                        moved = true
                    }
                }
            }
        }

        if (moved) {
            addRandomTile()
        }
    }

    override fun moveRight() {
        var moved = false

        for (i in matrix.indices) {
            var lastMergedPosition = -1

            for (j in matrixSize - 2 downTo 0) {
                if (matrix[i][j] != 0) {
                    var targetPosition = j + 1

                    while (targetPosition < matrixSize && matrix[i][targetPosition] == 0) {
                        matrix[i][targetPosition] = matrix[i][targetPosition - 1]
                        matrix[i][targetPosition - 1] = 0
                        targetPosition++
                        moved = true
                    }

                    if (targetPosition < matrixSize && matrix[i][targetPosition] == matrix[i][targetPosition - 1]
                        && lastMergedPosition != targetPosition
                    ) {
                        matrix[i][targetPosition] *= 2
                        score += matrix[i][targetPosition]
                        matrix[i][targetPosition - 1] = 0
                        lastMergedPosition = targetPosition
                        moved = true
                    }
                }
            }
        }

        if (moved) {
            addRandomTile()
        }
    }

    override fun moveDown() {
        var moved = false

        for (j in matrix[0].indices) {
            var lastMergedPosition = -1

            for (i in matrixSize - 2 downTo 0) {
                if (matrix[i][j] != 0) {
                    var targetPosition = i + 1

                    while (targetPosition < matrixSize && matrix[targetPosition][j] == 0) {
                        matrix[targetPosition][j] = matrix[targetPosition - 1][j]
                        matrix[targetPosition - 1][j] = 0
                        targetPosition++
                        moved = true
                    }

                    if (targetPosition < matrixSize && matrix[targetPosition][j] == matrix[targetPosition - 1][j]
                        && lastMergedPosition != targetPosition
                    ) {
                        matrix[targetPosition][j] *= 2
                        score += matrix[targetPosition][j]
                        matrix[targetPosition - 1][j] = 0
                        lastMergedPosition = targetPosition
                        moved = true
                    }
                }
            }
        }

        if (moved) {
            addRandomTile()
        }
    }

    override fun moveLeft() {
        var moved = false

        for (i in matrix.indices) {
            var lastMergedPosition = -1

            for (j in 1 until matrixSize) {
                if (matrix[i][j] != 0) {
                    var targetPosition = j - 1

                    while (targetPosition >= 0 && matrix[i][targetPosition] == 0) {
                        matrix[i][targetPosition] = matrix[i][targetPosition + 1]
                        matrix[i][targetPosition + 1] = 0
                        targetPosition--
                        moved = true
                    }

                    if (targetPosition >= 0 && matrix[i][targetPosition] == matrix[i][targetPosition + 1]
                        && lastMergedPosition != targetPosition
                    ) {
                        matrix[i][targetPosition] *= 2
                        score += matrix[i][targetPosition]
                        matrix[i][targetPosition + 1] = 0
                        lastMergedPosition = targetPosition
                        moved = true
                    }
                }
            }
        }

        if (moved) {
            addRandomTile()
        }
    }

    override fun getLastScore(): Int {
        score = pref.getLastScore()
        return score
    }

}
