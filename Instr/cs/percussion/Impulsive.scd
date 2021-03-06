/**
 *  @file       Impulsive.scd
 *
 *
 *  @author     Colin Sullivan <colin [at] colin-sullivan.net>
 *
 *  @copyright  2018 Colin Sullivan
 *  @license    Licensed under the GPLv3 license.
 **/

Instr("cs.percussion.Impulsive", {
  arg freq = 440,
    amp = 0.9,
    makeStereo = 0;

  var out,
    imp,
    env,
    harms = [
      (
        ratio: 4.875,
        rq: 0.1
      ),
      (
        ratio: 2.5,
        rq: 0.1
      ),
      /*(
        ratio: 2,
        rq: 0.01
      ),*/
      /*(
        ratio: 1.875,
        rq: 0.08
      ),*/
      (
        ratio: 1.5,
        rq: 0.06
      ),
      (
        ratio: 1,
        rq: 0.09
      )
    ];

  // a single impulse
  out = Impulse.ar(0);

  // filtered with resonances at harmonics
  harms.do({
    arg harm;


    out = RLPF.ar(out, freq*harm['ratio'], harm['rq']);
  });

  //env = EnvGen.kr(Env.new(
    //[1.0,    1.0,     0.0],
    //[     1.0,    0.1],
    //releaseNode: nil
  //), doneAction: doneAction);
  env = EnvGen.kr(Env.perc(0.001), doneAction: 2);

  out = amp * (env * out);

  if (makeStereo == 1, {
    [out, out];
  }, {
    out;
  });

}, [
  \freq,
  \amp,
  [0, 1]
]);
