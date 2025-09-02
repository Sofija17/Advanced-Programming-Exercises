package Second_Midterm_Exam;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

interface Observer {
    String update(WeatherDispatcher wd);
}

interface Subject {
    void register(Observer o);

    void remove(Observer o);

    void notifyObservers();
}

class WeatherDispatcher implements Subject {
    private List<Observer> observers;
    private float temperature;
    private float humidity;
    private float pressure;
    private boolean hasData = false;

    public WeatherDispatcher() {
        this.observers = new ArrayList<>();
    }

    public void setMeasurements(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        hasData = true;

        notifyObservers();
    }

    @Override
    public void register(Observer o) {
        if (!observers.contains(o)) {
            observers.add(o);
            // При (ре)регистрација, ако имаме тековни податоци – пушни моментален update
            if (hasData) {
                String out = o.update(this);
                if (out != null && !out.isEmpty()) {
                    System.out.println(out);
                    System.out.println();
                }
            }
        }
    }

    @Override
    public void remove(Observer o) {
        this.observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        boolean printed = false;
        for (Observer o : observers) {
            String out = o.update(this);
            if (out != null && !out.isEmpty()) {
                System.out.println(out);
                printed = true;
            }
        }
        if (printed) System.out.println();
    }

    public List<Observer> getObservers() {
        return observers;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getPressure() {
        return pressure;
    }
}

class CurrentConditionsDisplay implements Observer {
    private float temperature;
    private float humidity;
    private WeatherDispatcher wd;

    public CurrentConditionsDisplay(WeatherDispatcher wd) {
        wd.register(this);
        this.temperature = 0;
        this.humidity = 0;
        this.wd = wd;
    }

    @Override
    public String update(WeatherDispatcher wd) {
        this.temperature = wd.getTemperature();
        this.humidity = wd.getHumidity();

        return String.format("Temperature: %.1fF\nHumidity: %.1f%%", temperature, humidity);
    }

}

class ForecastDisplay implements Observer {
    private float pressure;
    private float lastPressure;
    private WeatherDispatcher wd;


    public ForecastDisplay(WeatherDispatcher wd) {
        wd.register(this);
        this.pressure = 0;
        this.wd = wd;
    }

    @Override
    public String update(WeatherDispatcher wd) {
        lastPressure = pressure;
        pressure = wd.getPressure();

        if (pressure > lastPressure) {
            return "Forecast: Improving";
        } else if (pressure < lastPressure) {
            return "Forecast: Cooler";
        } else {
            return "Forecast: Same";
        }
    }

}

public class WeatherApplication {

    public static void main(String[] args) {
        WeatherDispatcher weatherDispatcher = new WeatherDispatcher();

        CurrentConditionsDisplay currentConditions = new CurrentConditionsDisplay(weatherDispatcher);
        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherDispatcher);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            weatherDispatcher.setMeasurements(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]), Float.parseFloat(parts[2]));
            if (parts.length > 3) {
                int operation = Integer.parseInt(parts[3]);
                if (operation == 1) {
                    weatherDispatcher.remove(forecastDisplay);
                }
                if (operation == 2) {
                    weatherDispatcher.remove(currentConditions);
                }
                if (operation == 3) {
                    weatherDispatcher.register(forecastDisplay);
                }
                if (operation == 4) {
                    weatherDispatcher.register(currentConditions);
                }

            }
        }
    }
}