SamplerSynkopantsElement : SynkopantsElement {
  /**
   *  Rate of playback, -1 is backwards, etc.
   */
  var <>playbackRateControl,
    /**
     *  Which buffer (of the parent `BufferManager` instance) to use as this
     *  sampler.
     **/
    <>bufKey;

  init {
    arg params;

    super.init(params);

    this.bufKey = params['bufKey'];

    this.playbackRateControl = KrNumberEditor.new(1.0, ControlSpec(-4.0, 4.0));
  }


  init_patch {
    this.patch = Patch("cs.sfx.PlayBuf", (
      buf: this.parent.bufManager.bufs[this.bufKey],
      gate: 1,
      attackTime: 0.01,
      releaseTime: 0.01,
      amp: this.ampSlider,
      playbackRate: this.playbackRateControl
    ));
  }

  init_gui {
  
    arg params;

    var layout,
      labelWidth;

    super.init_gui(params);

    layout = params['layout'];
    labelWidth = params['labelWidth'];

    ArgNameLabel("playbackRate", layout, labelWidth);
    this.playbackRateControl.gui(layout);
    layout.startRow();

  }
}
