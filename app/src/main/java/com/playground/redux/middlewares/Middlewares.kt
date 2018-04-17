package com.playground.redux.middlewares

import com.playground.redux.actions.*
import com.playground.redux.appstate.AppState
import com.playground.redux.data.GitHubRepo
import com.playground.redux.data.GitHubRepoEntity
import com.playground.redux.navigation.Navigator
import com.playground.redux.navigation.Page
import com.playground.redux.network.GitHubEndpoint
import com.playground.redux.repository.Repository
import com.playground.redux.repository.gitrepo.GitRepoSpecification
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import tw.geothings.rekotlin.DispatchFunction
import tw.geothings.rekotlin.Middleware
import java.util.*
import javax.inject.Named

internal val loggingMiddleware: Middleware<AppState> = { dispatch, _ ->
    { next ->
        { action ->
            Timber.d("Action: -> " + action::class.java.simpleName)
            next(action)
        }
    }
}

fun navigationMiddleware(navigator: Navigator): Middleware<AppState> = { dispatch, _ ->
    { next ->
        { action ->
            when (action) {
                is SelectUserAction -> dispatch(NextPageAction(navigator.goNextPage(Page.USER_SELECT_PAGE)))
            }
            next(action)
        }
    }
}

fun reposMiddleware(endpoint: GitHubEndpoint, @Named("GIT_REPO") repository: Repository<GitHubRepoEntity>): Middleware<AppState> = { dispatch, _ ->
    { next ->
        { action ->
            when (action) {
                is LoadReposAction -> {
                    endpoint.getRepos(action.userName)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    { result -> handleGitHubRepoResult(dispatch, action.userName, result) },
                                    { error -> handleGitHubRepoError(dispatch, error.message)}
                            )
                }
                is LoadFavouriteInfoFromDbAction -> handleLoadFromDatabase(dispatch, repository, action.userName, action.repoList)
                is SaveFavouriteAction -> repository.add(GitHubRepoEntity(action.userName, action.repoName, true))
                is RemoveFavouriteAction -> repository.remove(GitHubRepoEntity(action.userName, action.repoName, true))
            }
            next(action)
        }
    }
}

fun handleLoadFromDatabase(dispatch: DispatchFunction, repository: Repository<GitHubRepoEntity>, userName: String, repoList: List<GitHubRepo>) {
    repository.query(GitRepoSpecification(userName)).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                    { result -> handleSuccessLoadFromDb(dispatch, result, repoList) },
                    { error -> handleGitHubRepoError(dispatch, "Error loading from DB " + error.message) }
            )
}

fun handleSuccessLoadFromDb(dispatch: DispatchFunction, result: List<GitHubRepoEntity>, repoList: List<GitHubRepo>) {
    val resultMap = result.map { it.repoName to it }.toMap()
    val updatedList = repoList.map { it -> it.copy(favorite = resultMap.contains(it.name)) }.toList()
    dispatch(GitHubReposSuccessAction(updatedList))
}

fun handleGitHubRepoResult(dispatch: DispatchFunction, userName: String, repoList: List<GitHubRepo>) {
    dispatch(GitHubReposSuccessAction(repoList))
    dispatch(LoadFavouriteInfoFromDbAction(repoList, userName))
    Timber.d("GitHubRepos success Network Call")
}

fun handleGitHubRepoError(dispatch: DispatchFunction, message: String?) {
    dispatch(GitHubReposFailedAction())
    Timber.e(message)
}

internal val userMiddleware: Middleware<AppState> = { dispatch, _ ->
    { next ->
        { action ->
            when (action) {
                is SelectUserAction -> dispatch(AddHistoryAction(action.selectedUser))
            }
            next(action)
        }
    }
}