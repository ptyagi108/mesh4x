class CreateFeeds < ActiveRecord::Migration
  def self.up
    create_table :feeds do |t|
      t.integer :mesh_id
      t.string :name

      t.timestamps
    end
  end

  def self.down
    drop_table :feeds
  end
end
