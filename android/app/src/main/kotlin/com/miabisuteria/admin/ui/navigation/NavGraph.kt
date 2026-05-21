package com.miabisuteri.admin.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.miabisuteri.admin.ui.auth.LoginScreen
import com.miabisuteri.admin.ui.calculator.CalculatorScreen
import com.miabisuteri.admin.ui.config.ConfigScreen
import com.miabisuteri.admin.ui.dashboard.DashboardScreen
import com.miabisuteri.admin.ui.orders.OrderDetailScreen
import com.miabisuteri.admin.ui.orders.OrderListScreen
import com.miabisuteri.admin.ui.products.ProductEditScreen
import com.miabisuteri.admin.ui.products.ProductListScreen
import com.miabisuteri.admin.util.SessionManager

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Dashboard : Screen("dashboard")
    object ProductList : Screen("products")
    object ProductEdit : Screen("products/edit?id={id}") {
        fun route(id: Int? = null) = if (id != null) "products/edit?id=$id" else "products/edit"
    }
    object OrderList : Screen("orders")
    object OrderDetail : Screen("orders/{orderId}") {
        fun route(orderId: String) = "orders/$orderId"
    }
    object Calculator : Screen("calculator?orderId={orderId}") {
        fun route(orderId: String? = null) =
            if (orderId != null) "calculator?orderId=$orderId" else "calculator"
    }
    object Config : Screen("config")
}

@Composable
fun MiaNavGraph(
    sessionManager: SessionManager,
    navController: NavHostController = rememberNavController()
) {
    val isLoggedIn by sessionManager.isLoggedIn.collectAsStateWithLifecycle()

    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Screen.Dashboard.route else Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(onLoginSuccess = {
                navController.navigate(Screen.Dashboard.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            })
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateProducts = { navController.navigate(Screen.ProductList.route) },
                onNavigateOrders = { navController.navigate(Screen.OrderList.route) },
                onNavigateConfig = { navController.navigate(Screen.Config.route) },
                onNavigateCalculator = { navController.navigate(Screen.Calculator.route()) },
                onLogout = {
                    sessionManager.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.ProductList.route) {
            ProductListScreen(
                onNavigateBack = { navController.popBackStack() },
                onEditProduct = { id -> navController.navigate(Screen.ProductEdit.route(id)) },
                onNewProduct = { navController.navigate(Screen.ProductEdit.route()) }
            )
        }

        composable(
            route = "products/edit?id={id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val idStr = backStackEntry.arguments?.getString("id")
            val productId = idStr?.toIntOrNull()
            ProductEditScreen(
                productId = productId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.OrderList.route) {
            OrderListScreen(
                onNavigateBack = { navController.popBackStack() },
                onOpenOrder = { orderId -> navController.navigate(Screen.OrderDetail.route(orderId)) }
            )
        }

        composable(
            route = "orders/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: return@composable
            OrderDetailScreen(
                orderId = orderId,
                onNavigateBack = { navController.popBackStack() },
                onOpenCalculator = { navController.navigate(Screen.Calculator.route(orderId)) }
            )
        }

        composable(
            route = "calculator?orderId={orderId}",
            arguments = listOf(navArgument("orderId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId")
            CalculatorScreen(
                orderId = orderId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Config.route) {
            ConfigScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
