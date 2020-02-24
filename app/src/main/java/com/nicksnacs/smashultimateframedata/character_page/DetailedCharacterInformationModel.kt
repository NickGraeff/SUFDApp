package com.nicksnacs.smashultimateframedata.character_page

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nicksnacs.smashultimateframedata.db.*
import com.nicksnacs.smashultimateframedata.net.SUFDApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleOnSubscribe
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.lang.IllegalStateException
import javax.inject.Inject

class DetailedCharacterInformationModel @Inject constructor(private val SUFDApiService: SUFDApiService, private val appDatabase: AppDatabase) : ViewModel() {
    companion object {
        private const val BASE_CHARACTER_URL: String = "https://ultimateframedata.com"
    }

    private val disposables = CompositeDisposable()
    val detailedCharacterInformation = MutableLiveData<DetailedCharacterInformation?>()

    fun loadCharacter(characterName: String, characterUrl: String) {
        val dbOp: Single<DetailedCharacterInformation> = Single.create { emitter ->
            try {
                emitter.onSuccess(appDatabase.detailedCharacterInformationGetter().getDetailedCharacterInformation(characterName))
            } catch (e: IllegalStateException) {
                emitter.onError(e)
            }
        }
        disposables.add(dbOp.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ info ->
                detailedCharacterInformation.value = info
            }, {
                // Network call to get data
                disposables.add(SUFDApiService.getCharacterPage(characterUrl)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ document ->
                        convertToCharacter(document).apply {
                            detailedCharacterInformation.value = this
                            val saveDbOp = Completable.create { emitter ->
                                appDatabase.detailedCharacterInformationGetter().insertDetailedCharacterInformation(this)
                                emitter.onComplete()
                            }
                            disposables.add(saveDbOp
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe {
                                    println("Successfully stored character details into db")
                                })
                        }
                    }, {
                        println("Error on Character GET")
                    })
                )
            }))
    }

    private fun convertToCharacter(document: Document): DetailedCharacterInformation {
        val element = document.body().getElementById("contentcontainer")
        val characterName = document.body().getElementsByClass("charactername")[0].ownText()
        return DetailedCharacterInformation(
            characterName,
            parseGroundAttacksFromElement(characterName, element.getElementById("groundattacks").nextElementSibling()),
            parseAerialAttacksFromElement(characterName, element.getElementById("aerialattacks").nextElementSibling()),
            parseSpecialAttacksFromElement(characterName, element.getElementById("specialattacks").nextElementSibling()),
            parseGrabsThrowsFromElement(characterName, element.getElementById("grabs").nextElementSibling()),
            parseDodgesRollsFromElement(characterName, element.getElementById("dodges").nextElementSibling()),
            parseMiscFromElement(characterName, element.getElementById("misc").nextElementSibling())
        )
    }

    private fun parseGroundAttacksFromElement(characterName: String, element: Element): List<GroundAttack> {
        val groundAttacks = ArrayList<GroundAttack>()
        for (moveContainer: Element in element.getElementsByClass("movecontainer")) {
            groundAttacks.add(
                GroundAttack(
                    characterName,
                    moveContainer.getElementsByClass("movename")[0].ownText(),
                    getNullableGif(moveContainer),
                    moveContainer.getElementsByClass("startup")[0].ownText(),
                    moveContainer.getElementsByClass("totalframes")[0].ownText(),
                    moveContainer.getElementsByClass("landinglag")[0].ownText(),
                    moveContainer.getElementsByClass("notes")[0].ownText(),
                    moveContainer.getElementsByClass("baseDamage")[0].ownText(),
                    moveContainer.getElementsByClass("shieldlag")[0].ownText(),
                    moveContainer.getElementsByClass("shieldstun")[0].ownText(),
                    moveContainer.getElementsByClass("whichhitbox")[0].ownText(),
                    moveContainer.getElementsByClass("advantage")[0].ownText(),
                    moveContainer.getElementsByClass("activeframes")[0].ownText()
                )
            )
        }
        return groundAttacks
    }

    private fun parseAerialAttacksFromElement(characterName: String, element: Element): List<AerialAttack> {
        val aerialAttacks = ArrayList<AerialAttack>()
        for (moveContainer: Element in element.getElementsByClass("movecontainer")) {
            aerialAttacks.add(
                AerialAttack(
                    characterName,
                    moveContainer.getElementsByClass("movename")[0].ownText(),
                    getNullableGif(moveContainer),
                    moveContainer.getElementsByClass("startup")[0].ownText(),
                    moveContainer.getElementsByClass("totalframes")[0].ownText(),
                    moveContainer.getElementsByClass("landinglag")[0].ownText(),
                    moveContainer.getElementsByClass("notes")[0].ownText(),
                    moveContainer.getElementsByClass("baseDamage")[0].ownText(),
                    moveContainer.getElementsByClass("shieldlag")[0].ownText(),
                    moveContainer.getElementsByClass("shieldstun")[0].ownText(),
                    moveContainer.getElementsByClass("whichhitbox")[0].ownText(),
                    moveContainer.getElementsByClass("advantage")[0].ownText(),
                    moveContainer.getElementsByClass("activeframes")[0].ownText()
                )
            )
        }
        return aerialAttacks
    }

    private fun parseSpecialAttacksFromElement(characterName: String, element: Element): List<SpecialAttack> {
        val specialAttacks = ArrayList<SpecialAttack>()
        for (moveContainer: Element in element.getElementsByClass("movecontainer")) {
            specialAttacks.add(
                SpecialAttack(
                    characterName,
                    moveContainer.getElementsByClass("movename")[0].ownText(),
                    getNullableGif(moveContainer),
                    moveContainer.getElementsByClass("startup")[0].ownText(),
                    moveContainer.getElementsByClass("totalframes")[0].ownText(),
                    moveContainer.getElementsByClass("landinglag")[0].ownText(),
                    moveContainer.getElementsByClass("notes")[0].ownText(),
                    moveContainer.getElementsByClass("baseDamage")[0].ownText(),
                    moveContainer.getElementsByClass("shieldlag")[0].ownText(),
                    moveContainer.getElementsByClass("shieldstun")[0].ownText(),
                    moveContainer.getElementsByClass("whichhitbox")[0].ownText(),
                    moveContainer.getElementsByClass("advantage")[0].ownText(),
                    moveContainer.getElementsByClass("activeframes")[0].ownText()
                )
            )
        }
        return specialAttacks
    }

    private fun parseGrabsThrowsFromElement(characterName: String, element: Element): List<GrabThrow> {
        val grabThrows = ArrayList<GrabThrow>()
        for (moveContainer: Element in element.getElementsByClass("movecontainer")) {
            grabThrows.add(
                GrabThrow(
                    characterName,
                    moveContainer.getElementsByClass("movename")[0].ownText(),
                    getNullableGif(moveContainer),
                    moveContainer.getElementsByClass("startup")[0].ownText(),
                    moveContainer.getElementsByClass("totalframes")[0].ownText(),
                    moveContainer.getElementsByClass("landinglag")[0].ownText(),
                    moveContainer.getElementsByClass("notes")[0].ownText(),
                    moveContainer.getElementsByClass("baseDamage")[0].ownText()
                )
            )
        }
        return grabThrows
    }

    private fun parseDodgesRollsFromElement(characterName: String, element: Element): List<DodgeRoll> {
        val dodgeRolls = ArrayList<DodgeRoll>()
        for (moveContainer: Element in element.getElementsByClass("movecontainer")) {
            dodgeRolls.add(
                DodgeRoll(
                    characterName,
                    moveContainer.getElementsByClass("movename")[0].ownText(),
                    getNullableGif(moveContainer),
                    moveContainer.getElementsByClass("totalframes")[0].ownText(),
                    moveContainer.getElementsByClass("landinglag")[0].ownText(),
                    moveContainer.getElementsByClass("notes")[0].ownText()
                )
            )
        }
        return dodgeRolls
    }

    private fun parseMiscFromElement(characterName: String, element: Element): List<Misc> {
        val misc = ArrayList<Misc>()
        for (moveContainer: Element in element.getElementsByClass("movecontainer")) {
            for (div: Element in moveContainer.children()) {
                misc.add(Misc(characterName, div.ownText()))
            }
        }
        return misc
    }

    private fun getNullableGif(moveContainer: Element) : String? {
        moveContainer.getElementsByClass("hitbox")?.apply {
            if (isNotEmpty()) {
                this[0].getElementsByClass("hitboximg")?.apply {
                    if (isNotEmpty()) {
                        return getCharacterUrl("""/${this[0].attr("data-featherlight")}""")
                    }
                }
            }
        }
        return null
    }

    private fun getCharacterUrl(characterUrl: String): String {
        return BASE_CHARACTER_URL + characterUrl
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}