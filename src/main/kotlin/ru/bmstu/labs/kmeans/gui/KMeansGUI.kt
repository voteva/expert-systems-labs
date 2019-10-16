package ru.bmstu.labs.kmeans.gui

import ru.bmstu.labs.kmeans.DoubleKMeans
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.event.ActionEvent
import java.io.*
import java.util.*
import javax.swing.*
import kotlin.random.Random

class KMeansGUI {
    private val toolBar: JToolBar
    private val nTextField: JTextField
    private val kTextField: JTextField
    private val equalCheckBox: JCheckBox
    private val canvaPanel: JPanel
    private var centroids: Array<DoubleArray> = arrayOf()
    private var points: Array<DoubleArray> = arrayOf()
    private var minmaxlens: Array<DoubleArray> = arrayOf()
    private var eKmeans: DoubleKMeans? = null
    private var lines: Array<String> = arrayOf()

    init {
        val frame = JFrame()
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.minimumSize = Dimension(RESOLUTION + 100, RESOLUTION + 100)
        frame.preferredSize = Dimension(RESOLUTION * 2, RESOLUTION * 2)

        val contentPanel = JPanel()
        contentPanel.layout = BorderLayout()
        frame.contentPane = contentPanel

        toolBar = JToolBar()
        toolBar.setFloatable(false)
        contentPanel.add(toolBar, BorderLayout.NORTH)

        val csvImportButton = JButton()
        csvImportButton.action = object : AbstractAction(" Import CSV ") {
            override fun actionPerformed(ae: ActionEvent) {
                csvImport()
            }
        }
        toolBar.add(csvImportButton)

        val csvExportButton = JButton()
        csvExportButton.action = object : AbstractAction(" Export CSV ") {
            override fun actionPerformed(ae: ActionEvent) {
                csvExport()
            }
        }
        toolBar.add(csvExportButton)

        val nLabel = JLabel(" Elements (N):")
        toolBar.add(nLabel)

        nTextField = JTextField("1000")
        toolBar.add(nTextField)

        val randomButton = JButton()
        randomButton.action = object : AbstractAction(" Random ") {
            override fun actionPerformed(ae: ActionEvent) {
                random()
            }
        }
        toolBar.add(randomButton)

        val kLabel = JLabel(" Clusters (K):")
        toolBar.add(kLabel)

        kTextField = JTextField("5")
        toolBar.add(kTextField)

        val equalLabel = JLabel(" Equal:")
        toolBar.add(equalLabel)

        equalCheckBox = JCheckBox("")
        toolBar.add(equalCheckBox)

        val runButton = JButton()
        runButton.action = object : AbstractAction(" Start ") {
            override fun actionPerformed(ae: ActionEvent) {
                start()
            }
        }
        toolBar.add(runButton)

        val clearButton = JButton()
        clearButton.action = object : AbstractAction(" Clear ") {
            override fun actionPerformed(ae: ActionEvent) {
                clear()
            }
        }
        toolBar.add(clearButton)

        canvaPanel = object : JPanel() {
            override fun paint(g: Graphics) {
                this@KMeansGUI.paint(g, getWidth(), getHeight())
            }
        }
        contentPanel.add(canvaPanel, BorderLayout.CENTER)

        frame.pack()
        frame.isVisible = true
    }

    private fun enableToolBar(enabled: Boolean) {
        for (component in toolBar.components) {
            component.isEnabled = enabled
        }
    }

    private fun csvImport() {
        enableToolBar(false)
        lines = arrayOf()
        points = arrayOf()
        try {
            val chooser = JFileChooser()
            val returnVal = chooser.showOpenDialog(toolBar)
            if (returnVal != JFileChooser.APPROVE_OPTION) {
                return
            }
            minmaxlens = arrayOf(doubleArrayOf(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), doubleArrayOf(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY), doubleArrayOf(0.0, 0.0))
            val points: MutableList<DoubleArray> = mutableListOf()
            val lines: MutableList<String> = mutableListOf()
            val reader = BufferedReader(FileReader(chooser.getSelectedFile()))
            reader.lineSequence().forEach {
                lines.add(it)
                val pointString = it.split(",")
                val point = DoubleArray(2)
                point[X] = pointString[X].trim().toDouble()
                point[Y] = pointString[Y].trim().toDouble()
                points.add(point)
                if (point[X] < minmaxlens[MIN][X]) {
                    minmaxlens[MIN][X] = point[X]
                }
                if (point[Y] < minmaxlens[MIN][Y]) {
                    minmaxlens[MIN][Y] = point[Y]
                }
                if (point[X] > minmaxlens[MAX][X]) {
                    minmaxlens[MAX][X] = point[X]
                }
                if (point[Y] > minmaxlens[MAX][Y]) {
                    minmaxlens[MAX][Y] = point[Y]
                }
            }
            minmaxlens[LEN][X] = minmaxlens[MAX][X] - minmaxlens[MIN][X]
            minmaxlens[LEN][Y] = minmaxlens[MAX][Y] - minmaxlens[MIN][Y]
            reader.close()
            this.points = points.toTypedArray()
            nTextField.text = (this.points.size).toString()
            this.lines = lines.toTypedArray()
        } catch (e: Exception) {
            e.printStackTrace(System.err)
        } finally {
            canvaPanel.repaint()
            enableToolBar(true)
        }
    }

    private fun csvExport() {
        if (eKmeans == null) {
            return
        }
        enableToolBar(false)
        try {
            val chooser = JFileChooser()
            val returnVal = chooser.showSaveDialog(toolBar)
            if (returnVal != JFileChooser.APPROVE_OPTION) {
                return
            }
            val writer = PrintWriter(BufferedWriter(FileWriter(chooser.getSelectedFile())))
            val points = this.points
            val assignments = eKmeans!!.assignments
            if (lines.isNotEmpty()) {
                for (i in points.indices) {
                    writer.printf(Locale.ENGLISH, "%d,%s%n", assignments[i], lines[i])
                }
            } else {
                for (i in points.indices) {
                    writer.printf(Locale.ENGLISH, "%d,%f,%f%n", assignments[i], points[i][X], points[i][Y])
                }
            }
            writer.flush()
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace(System.err)
        } finally {
            canvaPanel.repaint()
            enableToolBar(true)
        }
    }

    private fun random() {
        enableToolBar(false)
        eKmeans = null
        lines = arrayOf()
        val n = Integer.parseInt(nTextField.getText())
        points = Array(n) { DoubleArray(2) }
        minmaxlens = arrayOf(doubleArrayOf(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), doubleArrayOf(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY), doubleArrayOf(0.0, 0.0))
        for (i in 0 until n) {
            points[i][X] = RANDOM.nextDouble()
            points[i][Y] = RANDOM.nextDouble()
            if (points[i][X] < minmaxlens[MIN][X]) {
                minmaxlens[MIN][X] = points[i][X]
            }
            if (points[i][Y] < minmaxlens[MIN][Y]) {
                minmaxlens[MIN][Y] = points[i][Y]
            }
            if (points[i][X] > minmaxlens[MAX][X]) {
                minmaxlens[MAX][X] = points[i][X]
            }
            if (points[i][Y] > minmaxlens[MAX][Y]) {
                minmaxlens[MAX][Y] = points[i][Y]
            }
        }
        minmaxlens[LEN][X] = minmaxlens[MAX][X] - minmaxlens[MIN][X]
        minmaxlens[LEN][Y] = minmaxlens[MAX][Y] - minmaxlens[MIN][Y]
        canvaPanel.repaint()
        enableToolBar(true)
    }

    private fun start() {
        if (points.isEmpty()) {
            random()
        }
        Thread(Runnable {
            enableToolBar(false)
            try {
                this@KMeansGUI.run()
            } finally {
                enableToolBar(true)
            }
        }).start()
    }

    private fun clear() {
        points = arrayOf()
        centroids = arrayOf()
        minmaxlens = arrayOf()
        lines = arrayOf()
        eKmeans = null
        canvaPanel.repaint()
    }

    private fun run() {
        val k = Integer.parseInt(kTextField.getText())
        val equal = equalCheckBox.isSelected()

        centroids = Array(k) { DoubleArray(2) }
        for (i in 0 until k) {
            //            centroids[i][X] = minmaxlens[MIN][X] + (minmaxlens[LEN][X] * RANDOM.nextDouble());
            //            centroids[i][Y] = minmaxlens[MIN][Y] + (minmaxlens[LEN][Y] * RANDOM.nextDouble());
            centroids[i][X] = minmaxlens[MIN][X] + minmaxlens[LEN][X] / 2.0
            centroids[i][Y] = minmaxlens[MIN][Y] + minmaxlens[LEN][Y] / 2.0
        }
        eKmeans = DoubleKMeans(centroids, points, equal, DoubleKMeans.EUCLIDEAN_DISTANCE_FUNCTION)
        eKmeans!!.run()
        canvaPanel.repaint()
    }

    private fun paint(g: Graphics, width: Int, height: Int) {
        g.color = Color.WHITE
        g.fillRect(0, 0, width, height)
        if (minmaxlens.isEmpty()) {
            return
        }
        val widthRatio = (width - 6.0) / minmaxlens[LEN][X]
        val heightRatio = (height - 6.0) / minmaxlens[LEN][Y]
        if (points.isEmpty()) {
            return
        }
        g.color = Color.BLACK
        for (i in points.indices) {
            val px = 3 + (widthRatio * (points[i][X] - minmaxlens[MIN][X])).toInt()
            val py = 3 + (heightRatio * (points[i][Y] - minmaxlens[MIN][Y])).toInt()
            g.drawRect(px - 2, py - 2, 4, 4)
        }
        if (eKmeans == null) {
            return
        }
        val assignments = eKmeans!!.assignments
        val counts = eKmeans!!.counts
        val s = 225 / centroids.size
        for (i in points.indices) {
            val assignment = assignments[i]
            if (assignment == -1) {
                continue
            }
            val cx = 3 + (widthRatio * (centroids[assignment][X] - minmaxlens[MIN][X])).toInt()
            val cy = 3 + (heightRatio * (centroids[assignment][Y] - minmaxlens[MIN][Y])).toInt()
            val px = 3 + (widthRatio * (points[i][X] - minmaxlens[MIN][X])).toInt()
            val py = 3 + (heightRatio * (points[i][Y] - minmaxlens[MIN][Y])).toInt()
            val c = assignment * s
            g.color = Color(c, c, c)
            g.drawLine(cx, cy, px, py)
        }
        g.color = Color.GREEN
        for (i in centroids.indices) {
            val cx = 3 + (widthRatio * (centroids[i][X] - minmaxlens[MIN][X])).toInt()
            val cy = 3 + (heightRatio * (centroids[i][Y] - minmaxlens[MIN][Y])).toInt()
            g.drawLine(cx, cy - 2, cx, cy + 2)
            g.drawLine(cx - 2, cy, cx + 2, cy)
            val count = counts[i]
            g.drawString(count.toString(), cx, cy)
        }
    }

    companion object {
        private const val MIN = 0
        private const val MAX = 1
        private const val LEN = 2

        private const val X = 0
        private const val Y = 1

        private const val RESOLUTION = 300
        private val RANDOM = Random(System.currentTimeMillis())
    }
}