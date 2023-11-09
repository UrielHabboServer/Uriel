package org.urielserv.uriel.game.habbos.wardrobe.figure_data

import io.klogging.noCoLogger
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import javax.xml.parsers.DocumentBuilderFactory

class UrielFigureDataManager(pathUri: String) {

    private val logger = noCoLogger("uriel.game.habbos.wardrobe.figure_data.UrielFigureDataManager")

    val palettes: MutableMap<Int, FigureDataPalette> = mutableMapOf()
    val setTypes: MutableMap<String, FigureDataSetType> = mutableMapOf()

    init {
        val document = createDocument(pathUri)

        palettes.clear()
        setTypes.clear()

        if (!isDocumentValid(document)) {
            logger.error("The provided Figure Data document is invalid")
        } else {
            parsePalettes(getNodeList(document, "colors"))
            parseSetTypes(getNodeList(document, "sets"))
        }
    }

    private fun createDocument(pathUri: String): Document {
        val factory = DocumentBuilderFactory.newInstance()

        factory.isValidating = false
        factory.isIgnoringElementContentWhitespace = true

        val builder = factory.newDocumentBuilder()

        return builder.parse(pathUri)
    }

    private fun isDocumentValid(document: Document): Boolean {
        val rootElement = document.documentElement

        return rootElement.tagName.equals("figuredata", ignoreCase = true) &&
                document.getElementsByTagName("colors") != null &&
                document.getElementsByTagName("sets") != null
    }

    private fun getNodeList(document: Document, tagName: String) = document.getElementsByTagName(tagName).item(0).childNodes

    private fun parsePalettes(palettesList: NodeList) {
        palettesList.forEachNode { element ->
            val palette = createPalette(element)

            parseColors(element.childNodes) { color ->
                palette.colors[color.id ] = color
            }

            palettes[palette.id] = palette
        }
    }

    private fun createPalette(element: Element) = FigureDataPalette(element.getAttribute("id").toInt())

    private fun parseColors(colorsList: NodeList, action: (FigureDataPalette.Color) -> Unit) {
        colorsList.forEachNode { element ->
            action(
                FigureDataPalette.Color(
                    element.getAttribute("id").toInt(), element.getAttribute("index").toInt(),
                    element.getAttribute("club") != "0", element.getAttribute("selectable") == "1",
                    element.textContent
                )
            )
        }
    }

    private fun parseSetTypes(setTypesList: NodeList) {
        setTypesList.forEachNode { element ->
            val setType = createSetType(element)

            parseSets(element.childNodes) { set ->
                setType.sets[set.id] = set
            }

            setTypes[setType.type] = setType
        }
    }

    private fun createSetType(element: Element) = FigureDataSetType(
        element.getAttribute("type"), element.getAttribute("paletteid").toInt(),
        element.getAttribute("mand_m_0") == "1", element.getAttribute("mand_f_0") == "1",
        element.getAttribute("mand_m_1") == "1", element.getAttribute("mand_f_1") == "1"
    )

    private fun parseSets(setsList: NodeList, action: (FigureDataSetType.Set) -> Unit) {
        setsList.forEachNode { element ->
            action(
                FigureDataSetType.Set(
                    element.getAttribute("id").toInt(),
                    element.getAttribute("gender"),
                    element.getAttribute("club") != "0",
                    element.getAttribute("colorable") == "1",
                    element.getAttribute("selectable") == "1",
                    element.getAttribute("preselectable") == "1",
                    element.getAttribute("sellable") == "1"
                )
            )
        }
    }

    private fun NodeList.forEachNode(action: (Element) -> Unit) {
        for (i in 0..<length) {
            val node = item(i)

            if (node.nodeType == Node.ELEMENT_NODE) {
                action(node as Element)
            }
        }
    }

}