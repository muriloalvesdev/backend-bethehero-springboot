package br.com.thehero.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "files")
public class Files {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID uuid;

	@Column(name = "created_at")
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	protected LocalDateTime createdAt;

	@Column(name = "updated_at")
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	protected LocalDateTime updatedAt;

	private String filename;

	private byte[] data;

	private String type;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "incidents_uuid", referencedColumnName = "uuid", foreignKey = @ForeignKey(name = "uuid"))
	private Incidents Incidents;

	@SuppressWarnings("unused")
	private Files() {
	}

	public Files(String filename, byte[] data, String type, Incidents Incidents) {
		this.filename = filename;
		this.data = data;
		this.type = type;
		this.Incidents = Incidents;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Incidents getIncidents() {
		return Incidents;
	}

	public void setIncidents(Incidents incidents) {
		Incidents = incidents;
	}

	public UUID getUuid() {
		return uuid;
	}

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return this.updatedAt == null ? getCreatedAt() : this.updatedAt;
	}

	public static class FilesBuilder {
		private String filename;
		private byte[] data;
		private String type;
		private Incidents Incidents;

		private FilesBuilder(byte[] data) {
			this.data = data;
		}

		public static FilesBuilder newBuilder(byte[] data) {
			return new FilesBuilder(data);
		}

		public FilesBuilder withFilename(String filename) {
			this.filename = filename;
			return this;
		}

		public FilesBuilder withType(String type) {
			this.type = type;
			return this;
		}

		public FilesBuilder withIncidents(Incidents Incidents) {
			this.Incidents = Incidents;
			return this;
		}

		public Files build() {
			return new Files(filename, data, type, Incidents);
		}
	}
}
