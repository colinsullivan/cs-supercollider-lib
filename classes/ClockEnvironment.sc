ClockEnvironment : PerformanceEnvironmentComponent {

  var <>interface, <>clockFace, <>midiClockOut;

  start {
    clockFace.play();
    /*this.midiClockOut.play();*/
  }

  stop {
    clockFace.stop();
    /*this.midiClockOut.stop();*/
  }

  handle_reset_button_pressed {
    clockFace.cursecs = 0.0;
  }

  request_tempo_update {
    var tempoRequest;
    tempoRequest = NetAddr.new("127.0.0.1", 6667);
    tempoRequest.sendMsg("/cs/to_ableton/request_tempo_update");
  }

  init {
    arg params;

    var window,
      startPauseButton,
      resetButton,
      clockFace,
      me = this,
      tempoResponder;

    super.init(params);

    MIDIClient.init();
    MIDIPort.init();
    MIDIIn.connectAll();
    /*MIDIIn.connect(0, MIDIClient.sources[1]);*/
    // TODO: Figure out how to explicitly declare which MIDI port sync clock is using
    /*MIDISyncClock.init();*/

    /*MIDISyncClock.sched(10, {
      Tempo.tempo = MIDISyncClock.tempo.round();
      10;
    });*/
    /*this.midiClockOut = MIDIClockOut.new("(in) To Ableton Tempo", "(in) To Ableton Tempo");*/

    tempoResponder = OSCdef.new('clockTempoResponder', {
      arg msg;

      var bpm = msg[1];

      Tempo.bpm = bpm;

    }, '/cs/from_ableton/tempo_update', recvPort: 6666);



    clockFace = ClockFace.new();
    this.clockFace = clockFace;
    window = clockFace.window();
    window.bounds.height = window.bounds.height + 50;

    startPauseButton = Button(window, Rect(10, 80, 100, 30))
      .states_([
        ["start"],
        ["stop"]
      ])
      .action_({
        arg startPauseButton;

        if (startPauseButton.value == 1, {
          me.start();
        }, {
          me.stop();
        });

      });

    resetButton = Button(window, Rect(120, 80, 100, 30))
      .states_([
        ["reset"]
      ])
      .action_({
        arg resetButton;

        me.handle_reset_button_pressed(resetButton);
      });

    this.init_gui((
      window: window
    ));

    {this.init_done_callback.value()}.defer(2);
  }

}
