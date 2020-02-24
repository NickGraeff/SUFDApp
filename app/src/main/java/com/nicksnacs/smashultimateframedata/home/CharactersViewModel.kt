package com.nicksnacs.smashultimateframedata.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nicksnacs.smashultimateframedata.db.BasicCharacterInformation
import com.nicksnacs.smashultimateframedata.net.SUFDApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import javax.inject.Inject

class CharactersViewModel @Inject constructor(private val SUFDApiService: SUFDApiService) : ViewModel() {
    companion object {
        private const val BASE_IMAGE_URL: String = "https://ultimateframedata.com/characterart/"
    }

    private val disposables = CompositeDisposable()
    val characters = MutableLiveData<List<BasicCharacterInformation>?>()

    fun loadCharacters() {
        // Network call to get data
        disposables.add(SUFDApiService.homePage
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ document ->
                characters.value = convertToCharacters(document)
            }, {
                println("Error on Characters home GET")
            }))
    }

    private fun convertToCharacters(document: Document): List<BasicCharacterInformation> {
        val characters = ArrayList<BasicCharacterInformation>()
        val charList = document.body().getElementById("charList")
        for (e in charList.allElements) {
            if (e.hasClass("charactericon") &&
                !e.hasClass("stats")) {
                val linkElement = e.getElementsByTag("a")[0]
                characters.add(parseCharacterInfoFromElement(linkElement))
            }
        }
        return characters
    }

    private fun parseCharacterInfoFromElement(element: Element) : BasicCharacterInformation {
        val characterUrl = element.attr("href").split("/")[1]
        return BasicCharacterInformation(
            element.attr("title"),
            characterUrl,
            getCharacterImageUrl(characterUrl.split(".")[0])

        )
    }

    private fun getCharacterImageUrl(characterUrl: String) : String {
        return "$BASE_IMAGE_URL$characterUrl.jpg"
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
