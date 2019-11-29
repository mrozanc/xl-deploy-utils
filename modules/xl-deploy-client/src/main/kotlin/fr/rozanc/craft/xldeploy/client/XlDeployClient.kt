package fr.rozanc.craft.xldeploy.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.jackson.responseObject
import fr.rozanc.craft.xldeploy.objects.XlDeployReference
import java.io.File

@ExperimentalStdlibApi
fun main() {
    val answer = Fuel.get("http://localhost:4516/deployit/repository/v3/query")
            .authentication().basic("admin", "xldeploy")
            .header("Content-Type", "application/json;charset=UTF-8")
            .header("Accept", "application/json")
            .also { println(it) }
            .responseObject<List<XlDeployReference>>()
    println(answer.second)
    val refs = answer.third.component1()
    println(refs)
    buildTree(File("xldeploy"), refs)
}

fun buildTree(path: File, refs: List<XlDeployReference>?) {
    val yamlObjectMapper = ObjectMapper(YAMLFactory().enable(YAMLGenerator.Feature.MINIMIZE_QUOTES))

    path.mkdirs()
    if (refs == null) return

    val refsMap = refs.associate { it.ref to it.type }
    val items = ArrayList<XlDeployReference>()

    refs.forEach { ref ->
        if (ref.type in listOf("internal.Root", "core.Directory")) {
            File(path, ref.ref).mkdirs()
        } else {
            items += ref
        }
    }

    items.forEach {
        val answer = Fuel.get("http://localhost:4516/deployit/repository/ci/${it.ref}")
                .authentication().basic("admin", "xldeploy")
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("Accept", "application/json")
                .also { request -> println(request) }
                .responseObject<Map<String, *>>()
        println(answer.second)
        val lastDirectoryRef = getLastParentDirectory(refsMap, it.ref)
        File(path, lastDirectoryRef + "/" + it.ref.removePrefix("$lastDirectoryRef/").replace('/', '_') + ".yml")
                .writeBytes(yamlObjectMapper.writeValueAsBytes(answer.third.component1()?.filter { !it.key.startsWith('$') }))
    }
}

fun getLastParentDirectory(refsMap: Map<String, String>, ref: String): String {
    var currentRef = ref
    do {
        currentRef = currentRef.substringBeforeLast('/')
    } while (!listOf("internal.Root", "core.Directory").contains(refsMap[currentRef]))
    return currentRef
}
