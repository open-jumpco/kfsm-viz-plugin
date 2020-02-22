/*
 * Copyright (c) 2019. Open JumpCO
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * @suppress
 */
// tag::context[]
class Turnstile(locked: Boolean = true) {
    var locked: Boolean = locked
        private set

    fun unlock() {
        require(locked) { "Cannot unlock when not locked" }
        println("Unlock")
        locked = false
    }

    fun lock() {
        require(!locked) { "Cannot lock when locked" }
        println("Lock")
        locked = true
    }

    fun alarm() {
        println("Alarm")
    }

    fun returnCoin() {
        println("Return Coin")
    }

    override fun toString(): String {
        return "Turnstile(locked=$locked)"
    }
}
// end::context[]
/**
 * @suppress
 */
// tag::states-events[]
enum class TurnstileStates {
    LOCKED,
    UNLOCKED
}

/**
 * @suppress
 */
enum class TurnstileEvents {
    COIN,
    PASS
}
// end::states-events[]
/**
 * @suppress
 */
// tag::packaged[]
class TurnstileFSM(turnstile: Turnstile, savedState: TurnstileStates? = null) {
    private val fsm = definition.create(turnstile, savedState)

    fun externalState() = fsm.currentState
    fun coin() = fsm.sendEvent(TurnstileEvents.COIN)
    fun pass() = fsm.sendEvent(TurnstileEvents.PASS)
    fun allowedEvents() = fsm.allowed().map { it.name.toLowerCase() }.toSet()

    companion object {
        private val definition =
            stateMachine(
                TurnstileStates.values().toSet(),
                TurnstileEvents.values().toSet(),
                Turnstile::class
            ) {
                defaultInitialState = TurnstileStates.LOCKED
                initialState {
                    if (locked)
                        TurnstileStates.LOCKED
                    else
                        TurnstileStates.UNLOCKED
                }
                default {
                    onEntry { startState, targetState, _ ->
                        println("entering:$startState -> $targetState for $this")
                    }
                    action { state, event, _ ->
                        println("Default action for state($state) -> on($event) for $this")
                        alarm()
                    }
                    onExit { startState, targetState, _ ->
                        println("exiting:$startState -> $targetState for $this")
                    }
                }
                whenState(TurnstileStates.LOCKED) {
                    onEvent(TurnstileEvents.COIN to TurnstileStates.UNLOCKED) {
                        unlock()
                    }
                }
                whenState(TurnstileStates.UNLOCKED) {
                    onEvent(TurnstileEvents.COIN) {
                        returnCoin()
                    }
                    onEvent(TurnstileEvents.PASS to TurnstileStates.LOCKED) {
                        lock()
                    }
                }
            }.build()

        fun possibleEvents(state: TurnstileStates, includeDefault: Boolean = false) =
            definition.possibleEvents(state, includeDefault)
    }
}
// end::packaged[]
/**
 * @suppress
 */
class TurnstileAlternate(private val turnstile: Turnstile, initialState: TurnstileStates? = null) {
    var currentState: TurnstileStates
        private set

    init {
        currentState = initialState ?: if (turnstile.locked)
            TurnstileStates.LOCKED
        else
            TurnstileStates.UNLOCKED
    }

    private fun defaultEntry(startState: TurnstileStates, targetState: TurnstileStates, event: TurnstileEvents) {
        println("entering:$startState -> $targetState on($event) for $this")
    }

    private fun defaultExit(startState: TurnstileStates, targetState: TurnstileStates, event: TurnstileEvents) {
        println("exiting:$startState -> $targetState on($event) for $this")
    }

    private fun defaultAction(state: TurnstileStates, event: TurnstileEvents) {
        println("Default action for state($state) -> on($event) for $this")
        turnstile.alarm()
    }

    fun coin() {
        when (currentState) {
            TurnstileStates.LOCKED -> run {
                defaultExit(TurnstileStates.LOCKED, TurnstileStates.UNLOCKED, TurnstileEvents.COIN)
                turnstile.unlock()
                defaultEntry(TurnstileStates.LOCKED, TurnstileStates.UNLOCKED, TurnstileEvents.COIN)
                currentState = TurnstileStates.UNLOCKED
            }
            TurnstileStates.UNLOCKED -> run {
                turnstile.returnCoin()
            }
        }
    }

    fun pass() {
        when (currentState) {
            TurnstileStates.UNLOCKED -> run {
                defaultExit(TurnstileStates.UNLOCKED, TurnstileStates.LOCKED, TurnstileEvents.PASS)
                turnstile.lock()
                defaultEntry(TurnstileStates.UNLOCKED, TurnstileStates.LOCKED, TurnstileEvents.PASS)
                currentState = TurnstileStates.LOCKED
            }
            else -> defaultAction(currentState, TurnstileEvents.PASS)
        }
    }
}
