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
    var me = this;

    this.clockEnvironment = ClockEnvironment.new();
    this.granularChaosEnvironment = GranularChaosEnvironment.new();
    this.runningWaterEnvironment = RunningWaterEnvironment.new();
    /*this.sequencedGranularEnvironment = SequencedGranularEnvironment.new();*/
    this.dubBassEnvironment = DubBassEnvironment.new();

    {me.clockEnvironment.request_tempo_update();}.defer(2);
  }
}
