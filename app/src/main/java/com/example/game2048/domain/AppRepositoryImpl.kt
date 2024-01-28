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
                // Check right
                if (j < matrixSize - 1 && matrix[i][j] == matrix[i][j + 1]) {
                    return false
                }
                // Check down
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

    override fun getLastScore(): Int {
        return pref.getLastScore()
    }


    override fun moveRight() {
        matrix = matrix.map { slideToRight(it) }.toTypedArray()
        addRandomTile()
    }

    private fun slideToRight(row: Array<Int>): Array<Int> {
        val nRow = IntArray(matrixSize)
        var i = matrixSize - 1
        var j = matrixSize - 1

        while (i >= 0) {
            when {
                row[i] == 0 -> i--
                row[i] != 0 && nRow[j] == 0 -> {
                    nRow[j] = row[i]
                    i--
                }
                row[i] == nRow[j] -> {
                    nRow[j] += row[i]
                    score += nRow[j]
                    i--
                    j--
                }
                else -> j--
            }
        }

        return nRow.toTypedArray()
    }

    override fun moveLeft() {
        matrix = matrix.map { slideToLeft(it) }.toTypedArray()
        addRandomTile()
    }

    private fun slideToLeft(row: Array<Int>): Array<Int> {
        val nRow = IntArray(matrixSize)
        var i = 0
        var j = 0

        while (i < matrixSize) {
            when {
                row[i] == 0 -> i++
                row[i] != 0 && nRow[j] == 0 -> {
                    nRow[j] = row[i]
                    i++
                }
                row[i] == nRow[j] -> {
                    nRow[j] += row[i]
                    score += nRow[j]
                    i++
                    j++
                }
                else -> j++
            }
        }

        return nRow.toTypedArray()
    }

    override fun moveUp() {
        val newMatrix = Array(matrixSize) { Array(matrixSize) { 0 } }
        for (j in 0 until matrixSize) {
            val col = matrix.map { it[j] }.toIntArray()
            val newCol = slideUp(col)
            for (i in matrix.indices) {
                newMatrix[i][j] = newCol[i]
            }
        }
        matrix = newMatrix
        addRandomTile()
    }

    private fun slideUp(col: IntArray): IntArray {
        val nCol = IntArray(matrixSize)
        var i = 0
        var j = 0

        while (i < matrixSize) {
            when {
                col[i] == 0 -> i++
                col[i] != 0 && nCol[j] == 0 -> {
                    nCol[j] = col[i]
                    i++
                }
                col[i] == nCol[j] -> {
                    nCol[j] += col[i]
                    score += nCol[j]
                    i++
                    j++
                }
                else -> j++
            }
        }

        return nCol
    }

    override fun moveDown() {
        val newMatrix = Array(matrixSize) { Array(matrixSize) { 0 } }
        for (j in 0 until matrixSize) {
            val col = matrix.map { it[j] }.toIntArray()
            val newCol = slideDown(col)
            for (i in matrix.indices) {
                newMatrix[i][j] = newCol[i]
            }
        }
        matrix = newMatrix
        addRandomTile()
    }

    private fun slideDown(col: IntArray): IntArray {
        val nCol = IntArray(matrixSize)
        var i = matrixSize - 1
        var j = matrixSize - 1

        while (i >= 0) {
            when {
                col[i] == 0 -> i--
                col[i] != 0 && nCol[j] == 0 -> {
                    nCol[j] = col[i]
                    i--
                }
                col[i] == nCol[j] -> {
                    nCol[j] += col[i]
                    score += nCol[j]
                    i--
                    j--
                }
                else -> j--
            }
        }

        return nCol
    }
}
