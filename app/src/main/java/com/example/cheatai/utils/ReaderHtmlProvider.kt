package com.example.cheatai.utils

import androidx.compose.ui.graphics.Color
import com.example.cheatai.ui.theme.AppDimensions
import com.example.cheatai.ui.theme.DarkPinkSelection
import com.example.cheatai.ui.theme.PinkSelection

class ReaderHtmlProvider(
    private val dimensions: AppDimensions,
    private val bgHex: String,
    private val textHex: String,
    private val fontSizePx: Float,
    private val enoRegularBase64: String,
    private val enoBoldBase64: String
) {

    fun getGlobalStyles(): String {
        return """
                        <style>
                @font-face {
                    font-family: 'Eno';
                    src: url('data:font/truetype;base64,$enoRegularBase64') format('truetype');
                    font-weight: normal;
                    font-style: normal;
                }
                
                @font-face {
                    font-family: 'Eno';
                    src: url('data:font/truetype;base64,$enoBoldBase64') format('truetype');
                    font-weight: bold;
                    font-style: normal;
                }
                
                * {
                    font-family: 'Eno', 'Georgia', 'Times New Roman', serif !important;
                    color: $textHex !important;
                }
                
                html, body {
                    font-size: ${fontSizePx}px !important;
                    line-height: 1.1 !important;
                    background-color: $bgHex !important;
                    color: $textHex !important;
                    margin: 0 !important;
                    padding: 0px !important;
                }
                
                h1, h2, h3 {
                    font-weight: bold !important;
                    margin-bottom: 0.5em !important;
                }
                
                h1 { font-size: ${dimensions.textLarge.value * 1.2}px !important; }
                h2 { font-size: ${dimensions.textLarge.value}px !important; }
                
                p {
                    margin-bottom: 1em !important;
                    text-align: justify !important;
                }
                
                em, i { font-style: italic !important; }
                strong, b { font-weight: bold !important; }
            </style>
        """.trimIndent()
    }

    fun getTestChapter(chapterNumber: Int): String {
        val styles = getGlobalStyles()
        return """
            <html>
            <head>
                $styles
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body>
                <h1>Глава $chapterNumber</h1>
                <p>Мистер и миссис Дурсль проживали в доме номер четыре по Тисовой улице и всегда с гордостью заявляли, что они, слава богу, абсолютно нормальные люди. Уж от кого-кого, а от них никак нельзя было ожидать, чтобы они попали в какую-нибудь странную или загадочную ситуацию. Мистер и миссис Дурсль весьма неодобрительно относились к любым странностям, загадкам и прочей ерунде</p>
                <p>Мистер Дурсль возглавлял фирму под названием «Граннингс», которая специализировалась на производстве дрелей. Это был полный мужчина с очень пышными усами и очень короткой шеей. Что же касается миссис Дурсль, она была тощей блондинкой с шеей почти вдвое длиннее, чем положено при ее росте. Однако этот недостаток пришелся ей весьма кстати, поскольку большую часть времени миссис Дурсль следила за соседями и подслушивала их разговоры. А с такой шеей, как у нее, было очень удобно заглядывать за чужие заборы. У мистера и миссис Дурсль был маленький сын по имени Дадли, и, по их мнению, он был самым чудесным ребенком на свете.</p>
                <p>Семья Дурслей имела все, чего только можно пожелать. Но был у них и один секрет. Причем больше всего на свете они боялись, что кто-нибудь о нем узнает. Дурсли даже представить себе не могли, что с ними будет, если выплывет правда о Поттерах. Миссис Поттер приходилась миссис Дурсль родной сестрой, но они не виделись вот уже несколько лет. Миссис Дурсль даже делала вид, что у нее вовсе нет никакой сестры, потому что сестра и ее никчемный муж были полной противоположностью Дурслям. Дурсли содрогались при одной мысли о том, что скажут соседи, если на Тисовую улицу пожалуют Поттеры. Дурсли знали, что у Поттеров тоже есть маленький сын, но они никогда его не видели. И они категорически не хотели, чтобы их Дадли общался с ребенком таких родителей.</p>
                <p>Когда мистер и миссис Дурсли проснулись в одно скучное и серое утро во вторник — а именно с этого утра и начинается наша история, — ничто, включая покрытое тучами небо, не предвещало, что вскоре по всей стране начнут происходить странные и загадочные вещи. Мистер Дурсль что-то напевал себе под нос, выбирая для работы самый неприметный из своих галстуков. А миссис Дурсль, с трудом усадив сопротивляющегося и орущего Дадли на высокий детский стульчик, со счастливой улыбкой пересказывала мужу последние сплетни.</p>
                <p>Никто из них не заметил, как за окном пролетела большая сова-неясыть.</p>
                <p>В половине девятого мистер Дурсль взял свой портфель, клюнул миссис Дурсль в щеку и попытался на прощанье поцеловать Дадли, но промахнулся, потому что Дадли впал в ярость, что с ним происходило довольно часто. Он раскачивался взад-вперед на стульчике, ловко выуживал из тарелки кашу и заляпывал ею стены.</p>
                 <p>— Ух, ты моя крошка, — со смехом выдавил из себя мистер Дурсль, выходя из дома.</p>
                 <p> Он сел в машину и выехал со двора. </p>
                 <p>На углу улицы мистер Дурсль заметил, что происходит что-то странное, — на тротуаре стояла кошка и внимательно изучала лежащую перед ней карту. В первую секунду мистер Дурсль даже не понял, что именно он увидел, но затем, уже миновав кошку, затормозил и резко оглянулся. На углу Тисовой улицы действительно стояла полосатая кошка, но никакой карты видно не было. </p>
                 <p>— И привидится же такое! — буркнул мистер Дурсль. </p>
                 <p>Наверное, во всем были виноваты мрачное утро и тусклый свет фонаря. На всякий случай мистер Дурсль закрыл глаза, потом открыл их и уставился на кошку. А кошка уставилась на него. </p>
                 <p>Мистер Дурсль отвернулся и поехал дальше, продолжая следить за кошкой в зеркало заднего вида. Он заметил, что кошка читает табличку, на которой написано «Тисовая улица». Нет, конечно же, не читает, поспешно поправил он самого себя, а просто смотрит на табличку. Ведь кошки не умеют читать — равно как и изучать карты. </p>
            </body>
            </html>
        """.trimIndent()
    }

    fun getTestPages(): List<String> = emptyList()

    fun getFuncTestChapter(chapterNumber: Int,
                           highlightPlace: Boolean = false,
                           highlightDef: Boolean = false) : String{
        var highlightColorDef =  "rgba(0,0,0,0)"
        var highlightColorPlace =  "rgba(0,0,0,0)"
        if (highlightDef)
            {highlightColorDef = PinkSelection.toRgbaString()
            }
        if (highlightPlace)
            {highlightColorPlace = DarkPinkSelection.toRgbaString()
        }
        val styles = getGlobalStyles()
        val highlightedPlace = "<span style=\"background-color: $highlightColorPlace;  !important\">Лондону</span>"
        val highlightedDef = "<span style=\"background-color: $highlightColorDef; !important\">мантиях</span>"
        return """
            <html>
            <head>
                $styles
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
            </head>
            <body>
                <h1>Глава $chapterNumber</h1>
                <p>Мистер Дурсль потряс головой и попытался выбросить из нее кошку. И пока его автомобиль ехал к $highlightedPlace из пригорода, мистер Дурсль думал о крупном заказе на дрели, который рассчитывал сегодня получить.</p>
                <p>Но когда он подъехал к $highlightedPlace, заполнившие его голову дрели вылетели оттуда в мгновение ока, потому что, попав в обычную утреннюю автомобильную пробку и от нечего делать глядя по сторонам, мистер Дурсль заметил, что на улицах появилось множество очень странно одетых людей. Людей в $highlightedDef. Мистер Дурсль не переносил людей в нелепой одежде, да взять хотя бы нынешнюю молодежь, которая расхаживает черт знает в чем! И вот теперь эти, нарядившиеся по какой-то дурацкой моде.</p>
                <p>Мистер Дурсль забарабанил пальцами по рулю. Его взгляд упал на сгрудившихся неподалеку странных типов, оживленно шептавшихся друг с другом. Мистер Дурсль пришел в ярость, увидев, что некоторые из них совсем не молоды, — подумать только, один из мужчин выглядел даже старше него, а позволил себе облачиться в изумрудно-зеленую мантию! </p>
              
            </body>
            </html>
        """.trimIndent()

    }
}
fun Color.toRgbaString(): String {
    return "rgba(${(red * 255).toInt()}, ${(green * 255).toInt()}, ${(blue * 255).toInt()}, $alpha)"
}