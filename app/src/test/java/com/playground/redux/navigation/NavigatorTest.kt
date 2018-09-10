package com.playground.redux.navigation

import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

class NavigatorTest {

    @Test
    fun goNextPage() {
        val underTest = Navigator()

        Assert.assertTrue(underTest.goNextPage(Page.USER_SELECT_PAGE) == Page.REPO_SELECT_PAGE)
        Assert.assertTrue(underTest.goNextPage(Page.REPO_SELECT_PAGE) == Page.COMMIT_LIST_PAGE)
    }
}