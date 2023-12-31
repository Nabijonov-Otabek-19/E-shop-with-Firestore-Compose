package uz.gita.androidexam.presentation.screen.home.page.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.orbitmvi.orbit.compose.*
import uz.gita.androidexam.R
import uz.gita.androidexam.ui.component.LoadingComponent
import uz.gita.androidexam.ui.component.ProductsComponent
import uz.gita.androidexam.ui.component.SearchView
import uz.gita.androidexam.ui.theme.AndroidExamTheme
import uz.gita.androidexam.utils.logger

object HomePage : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(R.string.home_tab)
            val icon = rememberVectorPainter(Icons.Default.Home)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        val viewModel: HomePageContract.ViewModel = getViewModel<HomePageViewModel>()
        val uiState = viewModel.collectAsState()

        AndroidExamTheme {
            Surface(modifier = Modifier.fillMaxSize()) {
                Scaffold(
                    topBar = { TopBar(viewModel::onEventDispatcher) }
                ) {
                    HomePageContent(
                        uiState = uiState,
                        onEventDispatcher = viewModel::onEventDispatcher,
                        modifier = Modifier.padding(it)
                    )
                }
            }
        }

        viewModel.collectSideEffect {
            when (it) {
                is HomePageContract.SideEffect.HasError -> {
                    logger("HomePageScreen Error = " + it.message)
                }
            }
        }
    }
}

@Composable
fun TopBar(
    onEventDispatcher: (HomePageContract.Intent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            painter = painterResource(id = R.drawable.ic_menu),
            contentDescription = null
        )

        Image(
            modifier = Modifier
                .size(65.dp)
                .padding(horizontal = 16.dp),
            painter = painterResource(id = R.drawable.ic_profile),
            contentDescription = null
        )
    }
}

@Composable
fun HomePageContent(
    uiState: State<HomePageContract.UIState>,
    onEventDispatcher: (HomePageContract.Intent) -> Unit,
    modifier: Modifier
) {

    val textState = remember { mutableStateOf(TextFieldValue("")) }

    Box(modifier = modifier.fillMaxSize()) {
        when (uiState.value) {
            HomePageContract.UIState.Loading -> {
                LoadingComponent()
                onEventDispatcher.invoke(HomePageContract.Intent.LoadData)
            }

            is HomePageContract.UIState.PrepareData -> {
                val data = (uiState.value as HomePageContract.UIState.PrepareData).productsData
                logger("HomePage Product Data = ${data.size}")
                if (data.isEmpty() || data[0].productList.isEmpty()) {
                    Image(
                        modifier = Modifier
                            .size(150.dp)
                            .align(Alignment.Center),
                        painter = painterResource(id = R.drawable.ic_empty),
                        contentDescription = null
                    )
                } else if (data[0].productList.isNotEmpty()) {
                    LazyColumn {
                        item {
                            SearchView(
                                state = textState,
                                Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                            )
                        }

                        items(data.size) {
                            ProductsComponent(
                                data[it],
                                Modifier.padding(vertical = 4.dp, horizontal = 14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}