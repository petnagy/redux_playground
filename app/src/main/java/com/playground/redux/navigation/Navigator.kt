package com.playground.redux.navigation

class Navigator {

    val pages = listOf(Page.USER_SELECT_PAGE, Page.REPO_SELECT_PAGE)

    fun goNextPage(actualPage: Page): Page {
        var actualIndex = 0
        for (index in 0 until pages.size) {
            if (pages[index] == actualPage) {
                actualIndex = index
                break
            }
        }
        return if (actualIndex <= pages.size - 2) pages[actualIndex + 1] else pages[actualIndex]
    }
}