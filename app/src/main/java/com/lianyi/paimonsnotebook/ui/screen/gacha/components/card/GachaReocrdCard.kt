package com.lianyi.paimonsnotebook.ui.screen.gacha.components.card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.layout.card.MaterialCard
import com.lianyi.paimonsnotebook.common.components.media.NetworkImageForMetadata
import com.lianyi.paimonsnotebook.common.components.text.AutoSizeText
import com.lianyi.paimonsnotebook.common.components.text.TitleText
import com.lianyi.paimonsnotebook.common.components.widget.ExpansionIndicator
import com.lianyi.paimonsnotebook.common.database.gacha.data.GachaRecordOverview
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.extension.value.nonScaledSp
import com.lianyi.paimonsnotebook.ui.screen.gacha.components.chart.CircleRingChart
import com.lianyi.paimonsnotebook.ui.screen.gacha.components.chart.legend.ChartLegend
import com.lianyi.paimonsnotebook.ui.screen.gacha.components.chart.legend.ChartProgressBarLegend
import com.lianyi.paimonsnotebook.ui.screen.gacha.data.GachaOverviewListItem
import com.lianyi.paimonsnotebook.ui.screen.gacha.util.GachaRecordCardDisplayState
import com.lianyi.paimonsnotebook.ui.screen.items.components.item.list_card.ItemGridListCard
import com.lianyi.paimonsnotebook.ui.screen.items.data.ItemListCardData
import com.lianyi.paimonsnotebook.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GachaRecordCard(
    item: GachaRecordOverview.Item,
    gachaOverviewListItems: List<GachaOverviewListItem>
) {
    Column {
        val iconId by remember {
            derivedStateOf {
                when (item.cardDisplayState) {
                    GachaRecordCardDisplayState.List -> R.drawable.ic_histogram
                    GachaRecordCardDisplayState.Grid -> R.drawable.ic_grid
                    else -> R.drawable.ic_circle_empty
                }
            }
        }

        MaterialCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TitleText(text = item.uigfGachaTypeName, fontSize = 16.sp)

                Row {

                    Icon(painter = painterResource(
                        id = iconId
                    ),
                        contentDescription = null,
                        tint = Black_60,
                        modifier = Modifier
                            .radius(2.dp)
                            .size(24.dp)
                            .clickable {
                                item.cardDisplayState = when (item.cardDisplayState) {
                                    GachaRecordCardDisplayState.List -> GachaRecordCardDisplayState.Grid
                                    GachaRecordCardDisplayState.Grid -> GachaRecordCardDisplayState.None
                                    else -> GachaRecordCardDisplayState.List
                                }
                            })

                    Spacer(modifier = Modifier.width(12.dp))

                    ExpansionIndicator(expand = item.hideCardInfo) {
                        item.hideCardInfo = !item.hideCardInfo
                    }
                }
            }

            AnimatedVisibility(visible = !item.hideCardInfo) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))

                    PieChartContent(item)
                }
            }

            AnimatedVisibility(visible = item.cardDisplayState != GachaRecordCardDisplayState.None) {
                Spacer(modifier = Modifier.height(12.dp))
            }

            AnimatedVisibility(visible = item.cardDisplayState == GachaRecordCardDisplayState.List) {
                Column {
                    gachaOverviewListItems.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                NetworkImageForMetadata(
                                    url = item.iconUrl,
                                    modifier = Modifier
                                        .radius(4.dp)
                                        .size(40.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = item.name, fontSize = 14.sp, color = Black)
                            }
                            Text(text = "${item.count}", fontSize = 14.sp, color = Black)
                        }
                    }
                }
            }

            AnimatedVisibility(visible = item.cardDisplayState == GachaRecordCardDisplayState.Grid) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    gachaOverviewListItems.forEach { item ->
                        ItemGridListCard(
                            data = item,
                            itemListCardData = ItemListCardData(
                                iconUrl = item.iconUrl,
                                quality = item.rankType
                            ),
                            dataContent = "${item.count}",
                            onClick = {

                            }
                        )
                    }
                }
            }

        }
    }
}

@Composable
private fun PieChartContent(item: GachaRecordOverview.Item) {
    val Star5ProgressBarValue = item.gachaTimesMap[5] ?: 0

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CircleRingChart(
            title = "${item.totalCount}",
            description = "祈愿次数",
            values = item.ringChartValues,
            totalCount = item.totalCount
        )

        Spacer(modifier = Modifier.height(16.dp))

        AutoSizeText(
            text = "${item.minTime} - ${item.maxTime}",
            targetTextSize = 14.nonScaledSp(),
            color = Black_30,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        item.countMap.forEach { (star, count) ->
            ChartLegend(
                legendColor = item.colorMap[star] ?: Primary,
                name = "${star}星",
                value = "$count",
                percent = "${"%.2f".format(count.toFloat() / item.totalCount * 100)}%"
            )
        }

        ChartProgressBarLegend(
            name = "五星保底进度",
            value = "$Star5ProgressBarValue", progress = item.gachaProgressMap[5] ?: 0f,
            progressColor = GachaStar5Color
        )

        Spacer(modifier = Modifier.height(4.dp))

        ChartProgressBarLegend(
            name = "四星保底进度",
            value = "${item.gachaTimesMap[4] ?: 0}", progress = item.gachaProgressMap[4] ?: 0f,
            progressColor = GachaStar4Color2
        )
    }
}