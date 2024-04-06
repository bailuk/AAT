package ch.bailu.aat_lib.preferences

open class SolidStaticIndexList(storage: StorageInterface, key: String, private val labelList: Array<String>) : SolidIndexList(storage, key) {
    override fun length(): Int {
        return labelList.size
    }

    public override fun getValueAsString(index: Int): String {
        return labelList[index]
    }

    override fun getStringArray(): Array<String> {
        return labelList
    }
}
