package com.mamsky.stockalculator.android.screen.draft

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mamsky.stockalculator.android.screen.draft.Sectoral.getSubSectoral


object Sectoral {

    fun String.getSubSectoral(): List<Pair<String, String>> = when (this) {
        Sectoral.ALL.FINANCE.id-> FINANCE.all
        Sectoral.ALL.ENERGY.id -> ENERGY.all
        Sectoral.ALL.CYCLICAL.id -> CYCLICAL.all
        Sectoral.ALL.BASIC_MATERIAL.id -> BasicInd.all
        Sectoral.ALL.HEALTH.id -> HEALTH.all
        Sectoral.ALL.INDUSTRIES.id -> INDUSTRIES.all
        Sectoral.ALL.INFRASTRUCTURE.id -> INFRASTRUCTURE.all
        Sectoral.ALL.NONCYCLICAL.id -> NONCYCLICAL.all
        Sectoral.ALL.TECHNOLOGY.id -> TECHNOLOGY.all
        Sectoral.ALL.TRANSPORTATION.id -> TRANSPORTATION.all
        Sectoral.ALL.PROPERTY_REAL_ESTATE.id -> PROPERTY.all
        else -> emptyList()
    }

    enum class ALL(val id: String, val label: String) {
        ENERGY(Sector.ENERGY, "Energy"),
        CYCLICAL(Sector.CYCLICAL, "Cyclical"),
        BASIC_MATERIAL(Sector.BASIC_MATERIAL, "Basic Material"),
        FINANCE(Sector.FINANCE, "Finance"),
        HEALTH(Sector.HEALTH, "Health"),
        INDUSTRIES(Sector.INDUSTRIES, "Industries"),
        INFRASTRUCTURE(Sector.INFRASTRUCTURE, "Infrastructure"),
        NONCYCLICAL(Sector.NON_CYCLICAL, "Non Cyclical"),
        TECHNOLOGY(Sector.TECHNOLOGY, "Technology"),
        TRANSPORTATION(Sector.TRANSPORTATION, "Transportation"),
        PROPERTY_REAL_ESTATE(Sector.PROPERTY_REAL_ESTATE, "Property & Real Estate");

        companion object {
            val all = entries.map { Pair(it.id, it.label) }
        }
    }

    enum class ENERGY(val id: String, val label: String) {
        COAL(Sector.SubSector.COAL, "Coal"),
        OIL_AND_GAS(Sector.SubSector.OIL_AND_GAS, "Oil & Gas"),
        ALTERNATIVE_ENERGY(Sector.SubSector.ALTERNATIF_ENERGY, "Alternative Energy");
        companion object {
            val all = entries.map { Pair(it.id, it.label) }
        }
    }

    enum class CYCLICAL(val id: String, val label: String) {
        MEDIA_ENTERTAINMENT(Sector.SubSector.MEDIA_ENTERTAINMENT, "Media & Entertainment"),
        RETAIL(Sector.SubSector.RETAIL, "Retail"),
        CLOTHES_LUXURY_GOODS(Sector.SubSector.CLOTHES_LUXURY_GOODS, "Clothes & Luxury Goods"),
        AUTOMOTIVE_COMPONENT(Sector.SubSector.AUTOMOTIVE_COMPONENT, "Automotive Component"),
        RECREATIONAL_GOODS(Sector.SubSector.RECREATIONAL_GOODS, "Recreational Goods"),
        HOME_CONSUMABLES(Sector.SubSector.HOME_CONSUMABLES, "Home Consumables"),
        CONSUMER_SERVICE(Sector.SubSector.CONSUMER_SERVICE, "Consumer Service");
        companion object {
            val all = entries.map { Pair(it.id, it.label) }
        }
    }

    enum class BasicInd(val id: String, val label: String) {
        RAW_MATERIALS(Sector.SubSector.RAW_MATERIALS, "Raw Materials");
        companion object {
            val all = entries.map { Pair(it.id, it.label) }
        }
    }

    enum class FINANCE(val id: String, val label: String) {
        BANK(Sector.SubSector.BANK, "Bank"),
        INSURANCE(Sector.SubSector.INSURANCE, "Insurance"),
        FINANCING_SERVICE(Sector.SubSector.FINANCING_SERVICE, "Financing Service"),
        INVESTMENT_SERVICE(Sector.SubSector.INVESTMENT_SERVICE, "Investment Service"),
        HOLDING_AND_INVESTMENT_COMPANY(Sector.SubSector.HOLDING_AND_INVESTMENT_COMPANY, "Holding & Investment Company");

        companion object {
            val all = entries.map { Pair(it.id, it.label) }
        }

    }

    enum class HEALTH(val id: String, val label: String) {
        HEALTH_SERVICE(Sector.SubSector.HEALTH_SERVICE, "Health Service"),
        PHARMACEUTICAL_HEALTH_TECHNOLOGY(Sector.SubSector.PHARMACEUTICAL_HEALTH_TECHNOLOGY, "Pharmaceutical Health Technology");
        companion object {
            val all = entries.map { Pair(it.id, it.label) }
        }
    }

    enum class INDUSTRIES(val id: String, val label: String) {
        INDUSTRIAL_GOODS(Sector.SubSector.INDUSTRIAL_GOODS, "Industrial Goods"),
        MULTI_SECTOR_HOLDING_COMPANY(Sector.SubSector.MULTI_SECTOR_HOLDING_COMPANY, "Multi Sector Holding Company"),
        INDUSTRIAL_SERVICES(Sector.SubSector.INDUSTRIAL_SERVICES, "Industrial Services");
        companion object {
            val all = entries.map { Pair(it.id, it.label) }
        }
    }

    enum class INFRASTRUCTURE(val id: String, val label: String) {
        BUILDING_CONSTRUCTION(Sector.SubSector.BUILDING_CONSTRUCTION, "Building Construction"),
        TRANSPORTATION_INFRASTRUCTURE(Sector.SubSector.TRANSPORTATION_INFRASTRUCTURE, "Transportation Infrastructure"),
        TELECOMMUNICATION(Sector.SubSector.TELECOMMUNICATION, "Telecommunication"),
        UTILITIES(Sector.SubSector.UTILITIES, "Utilities");
        companion object {
            val all = entries.map { Pair(it.id, it.label) }
        }
    }

    enum class NONCYCLICAL(val id: String, val label: String) {
        RETAIL_PRIMARY_GOODS(Sector.SubSector.RETAIL_PRIMARY_GOODS, "Retail Primary Goods"),
        FOODS_AND_BEVERAGES(Sector.SubSector.FOODS_AND_BEVERAGES, "Foods & Beverages"),
        CONSUMER_NON_DURABLES(Sector.SubSector.CONSUMER_NON_DURABLES, "Consumer Non Durables"),
        CIGARETTE(Sector.SubSector.CIGARETTE, "Cigarette"),
        AGRICULTURE(Sector.SubSector.AGRICULTURE, "Agriculture");

        companion object {
            val all = entries.map { Pair(it.id, it.label) }
        }
    }

    enum class TECHNOLOGY(val id: String, val label: String) {
        IT_SOFTWARE_AND_SERVICES(Sector.SubSector.IT_SOFTWARE_AND_SERVICES, "IT Software & Services"),
        TECHNOLOGY_HARDWARE_AND_EQUIPMENT(Sector.SubSector.TECHNOLOGY_HARDWARE_AND_EQUIPMENT, "Technology Hardware & Equipment");

        companion object {
            val all = entries.map { Pair(it.id, it.label) }
        }
    }

    enum class TRANSPORTATION(val id:String, val label: String) {
        TRANSPORTATION2(Sector.SubSector.TRANSPORTATION, "Transportation"),
        LOGISTIC(Sector.SubSector.LOGISTIC, "Logistic");
        companion object {
            val all = entries.map { Pair(it.id, it.label) }
        }
    }

    enum class PROPERTY(val id:String, val label: String) {
        PROPERTY_REAL_ESTATE(Sector.SubSector.PROPERTY_REAL_ESTATE, "Property & Real Estate");
        companion object {
            val all = entries.map { Pair(it.id, it.label) }
        }
    }

}

internal object Sector {
    const val BASIC_MATERIAL = "BASIC_MATERIAL"
    const val CYCLICAL = "CYCLICAL"
    const val ENERGY = "ENERGY"
    const val FINANCE = "FINANCE"
    const val HEALTH = "HEALTH"
    const val INDUSTRIES = "INDUSTRIES"
    const val INFRASTRUCTURE = "INFRASTRUCTURE"
    const val NON_CYCLICAL = "NON_CYCLICAL"
    const val TECHNOLOGY = "TECHNOLOGY"
    const val TRANSPORTATION = "TRANSPORTATION"
    const val PROPERTY_REAL_ESTATE = "PROPERTY_REAL_ESTATE"

    object SubSector {
        // BASIC MATERIAL
        const val RAW_MATERIALS = "RAW_MATERIALS"

        // CYCLICAL
        const val MEDIA_ENTERTAINMENT = "MEDIA_ENTERTAINMENT"
        const val RETAIL = "RETAIL"
        const val CLOTHES_LUXURY_GOODS = "CLOTHES_LUXURY_GOODS"
        const val AUTOMOTIVE_COMPONENT = "AUTOMOTIVE_COMPONENT"
        const val RECREATIONAL_GOODS = "RECREATION_GOODS"
        const val HOME_CONSUMABLES = "HOME_CONSUMABLES"
        const val CONSUMER_SERVICE = "CONSUMER_SERVICE"

        // INDUSTRIES
        const val INDUSTRIAL_GOODS = "INDUSTRIAL_GOODS"
        const val MULTI_SECTOR_HOLDING_COMPANY = "MULTI_SECTOR_HOLDING_COMPANY"
        const val INDUSTRIAL_SERVICES = "INDUSTRIAL_SERVICES"

        // NON CYCLICAL
        const val RETAIL_PRIMARY_GOODS = "RETAIL_PRIMARY_GOODS" // Perdagangan ritel barang utama
        const val FOODS_AND_BEVERAGES = "FOODS_AND_BEVERAGES"
        const val CONSUMER_NON_DURABLES = "CONSUMER_NON_DURABLES" // Produk rumah tangga tidak tahan lama
        const val CIGARETTE = "CIGARETTE"
        const val AGRICULTURE = "AGRICULTURE"

        // ENERGY
        const val COAL = "COAL"
        const val OIL_AND_GAS = "OIL_AND_GAS"
        const val ALTERNATIF_ENERGY = "ALTERNATIF_ENERGY"

        /// FINANCE
        const val BANK = "BANK"
        const val INSURANCE = "INSURANCE"
        const val FINANCING_SERVICE = "FINANCING_SERVICE"
        const val INVESTMENT_SERVICE = "INVESTMENT_SERVICE"
        const val HOLDING_AND_INVESTMENT_COMPANY = "HOLDING_INVESTMENT_COMPANY"

        // HEALTH
        const val HEALTH_SERVICE = "HEALTH_SERVICE"
        const val PHARMACEUTICAL_HEALTH_TECHNOLOGY = "PHARMACEUTICAL_HEALTH_TECHNOLOGY"

        // Technology
        const val IT_SOFTWARE_AND_SERVICES = "IT_SOFTWARE_AND_SERVICES"
        const val TECHNOLOGY_HARDWARE_AND_EQUIPMENT = "TECHNOLOGY_HARDWARE_AND_EQUIPMENT"

        // INFRASTRUCTURE
        const val BUILDING_CONSTRUCTION = "BUILDING_CONSTRUCTION"
        const val TRANSPORTATION_INFRASTRUCTURE = "TRANSPORTATION_INFRASTRUCTURE"
        const val TELECOMMUNICATION = "TELECOMMUNICATION"
        const val UTILITIES = "UTILITIES"

        // TRANSPORTATION
        const val TRANSPORTATION = "TRANSPORTATION"
        const val LOGISTIC = "LOGISTIC"

        const val PROPERTY_REAL_ESTATE = "PROPERTY_REAL_ESTATE"

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectorModalAll(
    show: Boolean,
    selectedId: String,
    onDismiss: () -> Unit,
    onClickItem: (String, String) -> Unit,
) {
    SectorModalBottomSheet(
        list = Sectoral.ALL.all,
        selectedId = selectedId,
        show = show,
        title = "Sector",
        sheetState = rememberModalBottomSheetState(),
        onDismiss = onDismiss::invoke,
        onClickItem = onClickItem::invoke,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectorModalSub(
    show: Boolean,
    parentKey: String,
    selectedId: String,
    onDismiss: () -> Unit,
    onClickItem: (String, String) -> Unit,
) {
    SectorModalBottomSheet(
        list = parentKey.getSubSectoral(),
        selectedId = selectedId,
        show = show,
        title = "Sub Sector $parentKey",
        sheetState = rememberModalBottomSheetState(),
        onDismiss = onDismiss::invoke,
        onClickItem = onClickItem::invoke
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SectorModalBottomSheet(
    list: List<Pair<String, String>>,
    selectedId: String,
    show: Boolean,
    title: String,
    sheetState: SheetState = rememberModalBottomSheetState(),
    onDismiss: () -> Unit,
    onClickItem: (String, String) -> Unit,
) {
    if (show) {
        ModalBottomSheet(
            onDismissRequest = onDismiss::invoke,
            sheetState = sheetState
        ) {
            SubSectorModal(list, selectedId, title, onClickItem = { id, label ->
                onDismiss.invoke()
                onClickItem.invoke(id, label)
            })
        }
    }
}

@Composable
internal fun SubSectorModal(
    list: List<Pair<String, String>>,
    selectedId: String,
    title: String = "Sector",
    onClickItem: (String, String) -> Unit,
) {
    Surface(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
        LazyColumn(modifier = Modifier.padding(bottom = 20.dp)) {
            item {
                Text(title,
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp),
                    style = MaterialTheme.typography.titleMedium)
            }
            items(items = list) {
                Box(modifier = Modifier
                    .clickable { onClickItem.invoke(it.first, it.second) }
                    .padding(bottom = 4.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp),
                        text = it.second,
                        color = if (it.first.equals(selectedId, true)) Color.Red
                        else Color.Black
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ModalPrev2() {

    val key = Sectoral.ALL.CYCLICAL.id
    val list = key.getSubSectoral()
    SubSectorModal(list, selectedId = Sectoral.ENERGY.ALTERNATIVE_ENERGY.id) {_,_->}

}

