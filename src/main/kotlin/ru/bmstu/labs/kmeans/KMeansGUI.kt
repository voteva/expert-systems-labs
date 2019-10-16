package ru.bmstu.labs.kmeans

import ru.bmstu.labs.kmeans.AbstractKMeans.Listener
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.event.ActionEvent
import java.io.*
import java.net.MalformedURLException
import java.net.URL
import java.text.MessageFormat
import java.util.*
import javax.swing.*
import kotlin.random.Random

class KMeansGUI {
    private val toolBar: JToolBar
    private val nTextField: JTextField
    private val kTextField: JTextField
    private val equalCheckBox: JCheckBox
    private val debugTextField: JTextField
    private val canvaPanel: JPanel
    private var statusBar: JLabel
    private var centroids: Array<DoubleArray> = arrayOf()
    private var points: Array<DoubleArray> = arrayOf()
    private var minmaxlens: Array<DoubleArray> = arrayOf()
    private var eKmeans: DoubleEKmeansExt? = null
    private var lines: Array<String> = arrayOf()

    private inner class DoubleEKmeansExt(
            centroids: Array<DoubleArray>,
            points: Array<DoubleArray>,
            equal: Boolean,
            doubleDistanceFunction: DoubleDistanceFunction,
            listener: Listener?)
        : DoubleKMeans(centroids, points, equal, doubleDistanceFunction, listener)

    init {
        val frame = JFrame()
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        frame.setMinimumSize(Dimension(RESOLUTION + 100, RESOLUTION + 100))
        frame.setPreferredSize(Dimension(RESOLUTION * 2, RESOLUTION * 2))

        val contentPanel = JPanel()
        contentPanel.setLayout(BorderLayout())
        frame.setContentPane(contentPanel)

        toolBar = JToolBar()
        toolBar.setFloatable(false)
        contentPanel.add(toolBar, BorderLayout.NORTH)

        val csvImportButton = JButton()
        csvImportButton.setAction(object : AbstractAction(" Import CSV ") {
            override fun actionPerformed(ae: ActionEvent) {
                csvImport()
            }
        })
        toolBar.add(csvImportButton)

        val csvExportButton = JButton()
        csvExportButton.setAction(object : AbstractAction(" Export CSV ") {
            override fun actionPerformed(ae: ActionEvent) {
                csvExport()
            }
        })
        toolBar.add(csvExportButton)

        val nLabel = JLabel("n:")
        toolBar.add(nLabel)

        nTextField = JTextField("1000")
        toolBar.add(nTextField)

        val randomButton = JButton()
        randomButton.setAction(object : AbstractAction(" Random ") {
            override fun actionPerformed(ae: ActionEvent) {
                random()
            }
        })
        toolBar.add(randomButton)

        val kLabel = JLabel("k:")
        toolBar.add(kLabel)

        kTextField = JTextField("5")
        toolBar.add(kTextField)

        val equalLabel = JLabel("equal:")
        toolBar.add(equalLabel)

        equalCheckBox = JCheckBox("")
        toolBar.add(equalCheckBox)

        val debugLabel = JLabel("debug:")
        toolBar.add(debugLabel)

        debugTextField = JTextField("0")
        toolBar.add(debugTextField)

        val runButton = JButton()
        runButton.setAction(object : AbstractAction(" Start ") {
            override fun actionPerformed(ae: ActionEvent) {
                start()
            }
        })
        toolBar.add(runButton)

        canvaPanel = object : JPanel() {
            override fun paint(g: Graphics) {
                this@KMeansGUI.paint(g, getWidth(), getHeight())
            }
        }
        contentPanel.add(canvaPanel, BorderLayout.CENTER)

        statusBar = JLabel(" ")
        contentPanel.add(statusBar, BorderLayout.SOUTH)

        frame.pack()
        frame.setVisible(true)
    }

    private fun enableToolBar(enabled: Boolean) {
        for (c in toolBar.getComponents()) {
            c.setEnabled(enabled)
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
                println(point[X].toString() + ", " + point[Y])
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
            nTextField.setText((this.points.size).toString())
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

    private fun run() {
        try {
            val url = URL("http://staticmap.openstreetmap.de/staticmap.php?center=" + (minmaxlens[MIN][X] + minmaxlens[LEN][X] / 2.0) + "," + (minmaxlens[MIN][Y] + minmaxlens[LEN][Y] / 2.0) + "&zoom=14&size=" + canvaPanel.getWidth() + "x" + canvaPanel.getHeight() + "&maptype=mapnik")
            System.out.println("url:$url")
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }

        val k = Integer.parseInt(kTextField.getText())
        val equal = equalCheckBox.isSelected()
        var debugTmp = 0
        try {
            debugTmp = Integer.parseInt(debugTextField.getText())
        } catch (ignore: NumberFormatException) {
        }

        val debug = debugTmp
        centroids = Array(k) { DoubleArray(2) }
        for (i in 0 until k) {
            //            centroids[i][X] = minmaxlens[MIN][X] + (minmaxlens[LEN][X] * RANDOM.nextDouble());
            //            centroids[i][Y] = minmaxlens[MIN][Y] + (minmaxlens[LEN][Y] * RANDOM.nextDouble());
            centroids[i][X] = minmaxlens[MIN][X] + minmaxlens[LEN][X] / 2.0
            centroids[i][Y] = minmaxlens[MIN][Y] + minmaxlens[LEN][Y] / 2.0
        }
        var listener: Listener? = null
        if (debug > 0) {
            listener = object : Listener {
                override fun iteration(iteration: Int, move: Int) {
                    statusBar.setText(MessageFormat.format("iteration {0} move {1}", iteration, move))
                    canvaPanel.repaint()
                    try {
                        Thread.sleep(debug.toLong())
                    } catch (e: InterruptedException) {
                        throw RuntimeException(e)
                    }
                }
            }
        }
        eKmeans = DoubleEKmeansExt(centroids, points, equal, DoubleKMeans.EUCLIDEAN_DISTANCE_FUNCTION, listener)
        var time = System.currentTimeMillis()
        eKmeans!!.run()
        time = System.currentTimeMillis() - time
        statusBar.setText(MessageFormat.format("EKmeans run in {0}ms", time))
        canvaPanel.repaint()
    }

    private fun paint(g: Graphics, width: Int, height: Int) {
        g.setColor(Color.WHITE)
        g.fillRect(0, 0, width, height)
        if (minmaxlens.isEmpty()) {
            return
        }
        val widthRatio = (width - 6.0) / minmaxlens[LEN][X]
        val heightRatio = (height - 6.0) / minmaxlens[LEN][Y]
        if (points.isEmpty()) {
            return
        }
        g.setColor(Color.BLACK)
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
            g.setColor(Color(c, c, c))
            g.drawLine(cx, cy, px, py)
        }
        g.setColor(Color.GREEN)
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

        private val MIN = 0
        private val MAX = 1
        private val LEN = 2

        private val X = 0
        private val Y = 1

        private val RESOLUTION = 300
        private val RANDOM = Random(System.currentTimeMillis())

        @JvmStatic
        fun main(args: Array<String>) {
            try {
                KMeansGUI()
            } catch (e: java.lang.Exception) {
                print("Warning")
            }
        }
    }
}