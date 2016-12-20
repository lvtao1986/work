package com.emarbox.dsp.monitor.util;

import com.codahale.metrics.*;
import org.apache.commons.lang.math.NumberUtils;

import java.io.File;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * 性能度量工具类
 * Created by mrzhu on 15/5/21.
 */
public class MetricsUtil {
    public static final MetricRegistry metricsRegistry = new MetricRegistry();

    private static final String dataDir = Config.getString("metrics.data.dir");
    private static int frequencySecends = NumberUtils.createInteger(Config.getString("metrics.frequency.secends"));

    static{
        final CsvReporter reporter = CsvReporter.forRegistry(metricsRegistry)
//                .formatFor(Locale.SIMPLIFIED_CHINESE)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build(new File(dataDir));
        reporter.start(frequencySecends, TimeUnit.SECONDS);
    }
}
