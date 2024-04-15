package it.einjojo.akani.core.api.network;

/**
 * Represents a location on the network
 */
public record NetworkLocation(double x, double y, double z, double pitch, double yaw, String worldName,
                              Type type, String referenceName) {

    public static Builder of(double x, double y, double z, String worldName) {
        return new Builder().x(x).y(y).z(z).worldName(worldName);
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Whether the location is specific to a server, a group or unspecified
     */
    public enum Type {
        /**
         * The position belongs to a specific server
         */
        SERVER,
        /**
         * The position belongs to a server group
         */
        GROUP,
        /**
         * It does not matter on which server the player is.
         */
        UNSPECIFIED,
    }

    public static class Builder {
        private double x;
        private double y;
        private double z;
        private double pitch;
        private double yaw;
        private String worldName;
        private Type type = Type.UNSPECIFIED;
        private String referenceName;

        public Builder x(double x) {
            this.x = x;
            return this;
        }

        public Builder y(double y) {
            this.y = y;
            return this;
        }

        public Builder z(double z) {
            this.z = z;
            return this;
        }

        public Builder pitch(double pitch) {
            this.pitch = pitch;
            return this;
        }

        public Builder yaw(double yaw) {
            this.yaw = yaw;
            return this;
        }

        public Builder worldName(String worldName) {
            this.worldName = worldName;
            return this;
        }

        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        public Builder referenceName(String referenceName) {
            this.referenceName = referenceName;
            return this;
        }

        public NetworkLocation build() {
            return new NetworkLocation(x, y, z, pitch, yaw, worldName, type, referenceName);
        }
    }
}
