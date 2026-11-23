package aaaminecraft.modern_fantasy.logics.energy.core;

import java.util.*;

/**
 * <h1>EnergyNetworkPathfinderクラス</h1>
 *
 * エネルギーネットワーク内の転送経路を探索するクラス。
 *
 * <p>
 *     幅優先探索(BFS)を利用して、始点から終点までの
 *     接続経路を取得する。
 * </p>
 */
public class EnergyNetworkPathfinder {

    public Optional<List<EnergyConnection>> findPath(
            EnergyNetwork network,
            EnergyConnectionPoint start,
            EnergyConnectionPoint destination
    ) {
        Objects.requireNonNull(network, "Network cannot be null.");
        Objects.requireNonNull(destination, "Destination cannot be null.");
        Objects.requireNonNull(start, "Start cannot be null.");

        if (!network.containsPoint(start) || !network.containsPoint(destination)) return Optional.empty();
        if (start.equals(destination)) return Optional.of(List.of());

        Queue<EnergyConnectionPoint> queue = new ArrayDeque<>();
        Set<EnergyConnectionPoint> visited = new HashSet<>();

        Map<EnergyConnectionPoint, EnergyConnection> previousConnections = new HashMap<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            EnergyConnectionPoint current = queue.poll();

            for (EnergyConnection connection : network.getConnections(current)) {

                EnergyConnectionPoint next = connection.getOtherEnd(current);
                if (!visited.add(next)) continue;

                previousConnections.put(next, connection);

                if (next.equals(destination)) {
                    return Optional.of(reconstructPath(start, destination, previousConnections));
                }
                queue.add(next);
            }
        }

        return Optional.empty();
    }

    private List<EnergyConnection> reconstructPath(
            EnergyConnectionPoint start,
            EnergyConnectionPoint destination,
            Map<EnergyConnectionPoint, EnergyConnection> previousConnections
    ) {
        List<EnergyConnection> path = new ArrayList<>();

        EnergyConnectionPoint current = destination;

        while (!current.equals(start)) {
            EnergyConnection connection = previousConnections.get(current);

            if (connection == null) throw new IllegalArgumentException("Failed to reconstruct network path from " + current + " to " + destination);

            path.add(connection);
            current = connection.getOtherEnd(current);
        }

        Collections.reverse(path);
        return List.copyOf(path);
    }
}
