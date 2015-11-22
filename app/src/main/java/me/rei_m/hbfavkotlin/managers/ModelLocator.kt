package me.rei_m.hbfavkotlin.managers

import java.util.HashMap

public class ModelLocator private constructor() {

    companion object {
        private val showcase = HashMap<Tag, Any>();

        public enum class Tag {
            FAVORITE,
            OWN,
        }

        public fun register(tag: Tag, model: Any): Unit {
            showcase.put(tag, model)
        }

        public fun get(tag: Tag): Any = showcase[tag]!!
    }
}