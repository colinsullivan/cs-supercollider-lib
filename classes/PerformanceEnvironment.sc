PerformanceEnvironment : Object {

  var <>granularChaosEnvironment,
    <>runningWaterEnvironment,
    <>clockEnvironment,
    <>sequencedGranularEnvironment,
    <>dubBassEnvironment,
    <>rhodesVoicerEnvironment;

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
    this.dubBassEnvironment = DubbassVoicerEnvironment.new();
    this.rhodesVoicerEnvironment = RhodesVoicerEnvironment.new();

    {me.clockEnvironment.request_tempo_update();}.defer(2);
  }
}
