package sollecitom.libs.swissknife.web.client.info.domain

data class ClientInfo(val device: Device, val operatingSystem: OperatingSystem, val layoutEngine: LayoutEngine, val agent: Agent) {

    companion object {
        val unknown = ClientInfo(device = Device.unknown, operatingSystem = OperatingSystem.unknown, layoutEngine = LayoutEngine.unknown, agent = Agent.unknown)
    }
}