PerformanceEnvironment : Object {

  var <>granularChaosEnvironment,
    <>runningWaterEnvironment,
    <>clockEnvironment,
    <>sequencedGranularEnvironment,
    <>dubBassEnvironment;

  *new {
    arg modules = [];
    ^super.new.init();
  }

  init {
    arg modules;
    var me = this;

    this.load_modules();
  }

  load_modules {
    this.granularChaosEnvironment = GranularChaosEnvironment.new();
    this.runningWaterEnvironment = RunningWaterEnvironment.new();
    this.clockEnvironment = ClockEnvironment.new();
    /*this.sequencedGranularEnvironment = SequencedGranularEnvironment.new();*/
    this.dubBassEnvironment = DubBassEnvironment.new();
  }
}
