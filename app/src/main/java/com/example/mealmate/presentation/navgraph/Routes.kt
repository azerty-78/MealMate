package com.example.mealmate.presentation.navgraph

object Routes{
    const val AUTH_GRAPHROUTE = "auth_graph"
    const val HOME_GRAPHROUTE = "home_graph"
    const val SETTINGS_GRAPHROUTE = "settings_graph"
    const val PROFILE_GRAPHROUTE = "profile_graph"
    const val COMMUNITY_GRAPHROUTE = "community_graph"

    sealed class Screen(
        val route: String
    ){
        //route du graph authentification
        object SignInScreen : Screen("signin_screen")
        object SingUpScreen : Screen("singup_screen")
        object ForgotPasswordScreen : Screen("forgotpassword_screen")

        //route du graph home
        object HomeScreen : Screen("home_screen")
        //how to manage more route
        object TopicList : Screen("topic_list")
        object TopicDetail : Screen("topic_detail/{topicId}"){
            fun setRoute(topicId : Int) = "topic_detail/$topicId"
        }

        //route du graph community
        object CommunityScreen : Screen("community_screen")
        object ChatWithAIScreen : Screen("community_screen/chat_with_ai")

        //route du graph settings
        object SettingsScreen : Screen("settings_screen")

        //route du graph profile
        object ProfileScreen : Screen("profile_screen")
    }
}