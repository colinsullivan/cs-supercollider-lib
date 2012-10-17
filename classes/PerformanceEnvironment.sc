PerformanceEnvironment : Object {

  var <>granularChaosEnvironment,
    <>runningWaterEnvironment,
    <>clockEnvironment,
    <>sequencedGranularEnvironment;

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
    this.sequencedGranularEnvironment = SequencedGranularEnvironment.new();
  }
}
