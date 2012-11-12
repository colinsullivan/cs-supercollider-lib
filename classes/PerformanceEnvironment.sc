PerformanceEnvironment : Object {

  var <>granularChaosEnvironment,
    <>runningWaterEnvironment,
    <>clockEnvironment,
    <>sequencedGranularEnvironment,
    <>dubBassEnvironment,
    <>rhodesVoicerEnvironment,
    <>randomizedLazersEnvironment;

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
    var me = this,
      screenHeight = 960,
      screenWidth = 1440,
      instrX,
      instrY;

    instrY = 0;
    instrX = 0;
    this.clockEnvironment = ClockEnvironment.new((
      origin: instrX@screenHeight,
      init_done_callback: {

        instrX = me.clockEnvironment.window.bounds.width;

        me.granularChaosEnvironment = GranularChaosEnvironment.new((
          origin: instrX@instrY,
          init_done_callback: {

            instrY = instrY - 25 - me.granularChaosEnvironment.window.bounds.height;
            
            me.runningWaterEnvironment = RunningWaterEnvironment.new((
              origin: instrX@instrY,
              init_done_callback: {

                instrY = instrY + 400 - me.runningWaterEnvironment.window.bounds.height;
                
                me.dubBassEnvironment = DubbassVoicerEnvironment.new((
                  origin: instrX@instrY,
                ));

                instrY = instrY - 25 - me.dubBassEnvironment.window.bounds.height;

                me.rhodesVoicerEnvironment = RhodesVoicerEnvironment.new((
                  origin: instrX@instrY
                ));

                instrY = instrY - me.rhodesVoicerEnvironment.window.bounds.height;

                me.randomizedLazersEnvironment = RandomizedLazersEnvironment.new((
                  origin: instrX@instrY
                ));
              
              }
            ));

          }
        ));
      }
    ));
    /*this.sequencedGranularEnvironment = SequencedGranularEnvironment.new();*/

    {me.clockEnvironment.request_tempo_update();}.defer(2);
  }
}
